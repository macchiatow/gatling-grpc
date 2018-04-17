package me.macchiatow.gatling.grpc

import com.trueaccord.scalapb.{GeneratedMessage => GenM}
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.session.Expression
import me.macchiatow.gatling.grpc.action.GrpcActionBuilder
import me.macchiatow.gatling.grpc.protocol.GrpcProtocolBuilder

import scala.reflect.ClassTag

object Predef {

  def grpc(implicit configuration: GatlingConfiguration) = GrpcProtocolBuilder(configuration)

  def grpc[ReqT <: GenM, ResT <: GenM](requestName: Expression[String])(implicit reqT: ClassTag[ReqT]) =
    new GrpcActionBuilder[ReqT, ResT](requestName)

}
