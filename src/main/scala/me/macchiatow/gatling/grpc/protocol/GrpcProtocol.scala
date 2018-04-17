package me.macchiatow.gatling.grpc.protocol

import akka.actor.ActorSystem
import io.gatling.core.CoreComponents
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.protocol.{Protocol, ProtocolKey}

object GrpcProtocol {

  def apply(configuration: GatlingConfiguration): GrpcProtocol = GrpcProtocol(
    host = "",
    port = 0
  )

  val GrpcProtocolKey = new ProtocolKey {
    type Protocol = GrpcProtocol
    type Components = GrpcComponents

    def protocolClass: Class[io.gatling.core.protocol.Protocol] = classOf[GrpcProtocol].asInstanceOf[Class[io.gatling.core.protocol.Protocol]]

    def defaultProtocolValue(configuration: GatlingConfiguration): GrpcProtocol = GrpcProtocol(configuration)

    def newComponents(system: ActorSystem, coreComponents: CoreComponents): GrpcProtocol => GrpcComponents = GrpcComponents
  }
}

case class GrpcProtocol(host: String, port: Int) extends Protocol {

  def host(host: String): GrpcProtocol = copy(host = host)

  def port(port: Int): GrpcProtocol = copy(port = port)
}
