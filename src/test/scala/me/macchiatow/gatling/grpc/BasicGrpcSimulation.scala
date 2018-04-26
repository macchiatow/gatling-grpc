package me.macchiatow.gatling.grpc

import java.net.ServerSocket

import io.gatling.core.Predef.{Simulation, scenario, _}
import io.grpc.ServerBuilder
import me.macchiatow.gatling.grpc.Predef._
import me.macchiatow.gatling.grpc.protocol.GrpcProtocol

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps


class BasicGrpcSimulation extends Simulation {

  val port: Int = new ServerSocket(0).getLocalPort

  ServerBuilder
    .forPort(port)
    .addService(
      FundsServiceGrpc.bindService((_: FundsRequest) => Future.successful(FundsResponse(300)),ExecutionContext.global))
    .build()
    .start()

  val feeder: Feeder[FundsRequest] = Iterator.continually(Map(
    ("fundsRequest", FundsRequest(Seq("adr")))
  ))

  val protocol: GrpcProtocol = grpc.host("localhost").port(port)

  private val scn = scenario("BasicGrpcSimulation").
    feed(feeder).
    exec(
      grpc("request").
        blockingUnaryCall(FundsServiceGrpc.METHOD_FUNDS, "${fundsRequest}").
        check { response =>
          response.funds > 200
        }
    )

  setUp(scn.inject(constantUsersPerSec(100) during (5 seconds))).protocols(protocol)
}
