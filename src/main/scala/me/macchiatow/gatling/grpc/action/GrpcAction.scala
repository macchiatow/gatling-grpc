package me.macchiatow.gatling.grpc.action

import scalapb.grpc.Grpc.guavaFuture2ScalaFuture
import scalapb.{GeneratedMessage => GenM}
import io.gatling.commons.stats.{KO, OK}
import io.gatling.commons.util.ClockSingleton.nowMillis
import io.gatling.commons.validation.Validation
import io.gatling.core.CoreComponents
import io.gatling.core.action.{Action, ExitableAction}
import io.gatling.core.session.Session
import io.gatling.core.stats.StatsEngine
import io.gatling.core.stats.message.ResponseTimings
import io.gatling.core.util.NameGen
import io.grpc.CallOptions
import io.grpc.stub.ClientCalls
import me.macchiatow.gatling.grpc.action.GrpcActionBuilder.GrpcRequestAttributes

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps
import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

class GrpcAction[ReqT <: GenM, ResT <: GenM](
    grpcRequestAttributes: GrpcRequestAttributes[ReqT, ResT],
    checks: Seq[ResT => Boolean],
    coreComponents: CoreComponents,
    throttled: Boolean,
    val next: Action)(implicit reqTag: ClassTag[ReqT])
    extends ExitableAction
    with NameGen {

  override def statsEngine: StatsEngine = coreComponents.statsEngine

  override def name: String = genName("grpcRequest")

  def validateRequest[T](session: Session)(
      implicit reqT: ClassTag[T]): Validation[T] = {
    grpcRequestAttributes.request(session) flatMap {
      case req: T =>
        io.gatling.commons.validation.Success(req)
      case req =>
        val err =
          s"Feeder type mismatch: required $reqT, but found ${req.getClass}"
        statsEngine.reportUnbuildableRequest(session, name, err)
        io.gatling.commons.validation.Failure(err)
    }
  }

  def doCall(req: ReqT, doAsync: Boolean): Future[ResT] = {
    if (doAsync) {
      guavaFuture2ScalaFuture(
        ClientCalls.futureUnaryCall(
          grpcRequestAttributes.channel.newCall(
            grpcRequestAttributes.methodDescriptor,
            CallOptions.DEFAULT),
          req
        )
      )
    } else {
      Try {
        ClientCalls.blockingUnaryCall(
          grpcRequestAttributes.channel,
          grpcRequestAttributes.methodDescriptor,
          CallOptions.DEFAULT,
          req
        )
      } match {
        case scala.util.Success(response) => Future.successful(response)
        case scala.util.Failure(e)        => Future.failed(e)
      }
    }
  }

  override def execute(session: Session): Unit = recover(session) {
    validateRequest(session) flatMap { req =>
      grpcRequestAttributes.requestName(session) map { requestName =>
        val requestStartDate = nowMillis

        doCall(req, grpcRequestAttributes.doAsync) transform {
          case Success(response) if checks.forall(_(response)) =>
            scala.util.Success(response)
          case _: Success[ResT] => Failure(new AssertionError("check failed"))
          case failure          => failure
        } onComplete { t =>
          val requestEndDate = nowMillis

          val result = t.toEither
          statsEngine.logResponse(session,
                                  requestName,
                                  ResponseTimings(requestStartDate,
                                                  requestEndDate),
                                  if (result.isRight) OK else KO,
                                  None,
                                  result.left.toOption.map(_.getMessage))

          if (throttled) {
            coreComponents.throttler.throttle(session.scenario,
                                              () => next ! session)
          } else {
            next ! session
          }
        }
      }
    }
  }
}
