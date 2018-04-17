package me.macchiatow.gatling.grpc.protocol

import io.gatling.core.config.GatlingConfiguration

import scala.language.implicitConversions

object GrpcProtocolBuilder {

  implicit def toGrpcProtocol(builder: GrpcProtocolBuilder): GrpcProtocol = builder.build()

  def apply(configuration: GatlingConfiguration): GrpcProtocolBuilder = GrpcProtocolBuilder(GrpcProtocol(configuration))
}

case class GrpcProtocolBuilder(grpcProtocol: GrpcProtocol) {
  def build(): GrpcProtocol = grpcProtocol
}