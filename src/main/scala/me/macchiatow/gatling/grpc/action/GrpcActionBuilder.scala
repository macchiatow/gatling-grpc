package me.macchiatow.gatling.grpc.action

import com.trueaccord.scalapb.{GeneratedMessage => GenM}
import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.session.Expression
import io.gatling.core.structure.ScenarioContext
import io.grpc.{ManagedChannel, ManagedChannelBuilder, MethodDescriptor}
import me.macchiatow.gatling.grpc.action.GrpcActionBuilder.GrpcRequestAttributes
import me.macchiatow.gatling.grpc.protocol.{GrpcComponents, GrpcProtocol}

import scala.reflect.ClassTag

case class GrpcActionBuilder[ReqT <: GenM, ResT <: GenM](requestName: Expression[String],
                                                         methodDescriptor: MethodDescriptor[ReqT, ResT] = null,
                                                         doAsync: Boolean = false,
                                                         request: Expression[GenM] = null,
                                                         checks: Seq[ResT => Boolean] = Seq.empty)
                                                        (implicit reqT: ClassTag[ReqT]) extends ActionBuilder {

  def blockingUnaryCall(methodDescriptor: MethodDescriptor[ReqT, ResT], request: Expression[GenM]): GrpcActionBuilder[ReqT, ResT] =
    copy(methodDescriptor = methodDescriptor, request = request, doAsync = false)

  def asyncUnaryCall(methodDescriptor: MethodDescriptor[ReqT, ResT], request: Expression[GenM]): GrpcActionBuilder[ReqT, ResT] =
    copy(methodDescriptor = methodDescriptor, request = request, doAsync = true)

  def check(p: (ResT => Boolean)*): GrpcActionBuilder[ReqT, ResT] = copy(checks = checks ++ p)

  override def build(ctx: ScenarioContext, next: Action): Action = {
    assert(methodDescriptor != null && request != null, "methodDescriptor or request are not provided")

    import ctx.{coreComponents, protocolComponentsRegistry, throttled}

    val grpcComponents: GrpcComponents = protocolComponentsRegistry.components(GrpcProtocol.GrpcProtocolKey)

    import grpcComponents.GrpcProtocol.{host, port}
    val channel: ManagedChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build
    val grpcRequestAttributes = GrpcRequestAttributes(requestName, request, methodDescriptor, doAsync, channel)

    new GrpcAction[ReqT, ResT](grpcRequestAttributes, checks, coreComponents, throttled, next)
  }
}

object GrpcActionBuilder {

  case class GrpcRequestAttributes[ReqT, ResT](requestName: Expression[String],
                                               request: Expression[GenM],
                                               methodDescriptor: MethodDescriptor[ReqT, ResT],
                                               doAsync: Boolean,
                                               channel: ManagedChannel)
}
