name := "gatling-grpc"
organization := "me.macchiatow"
version := "2.3.1"

scalaVersion := "2.12.4"
parallelExecution in Test := false

lazy val gatlingVersion = "2.3.1"

import com.trueaccord.scalapb.compiler.Version.scalapbVersion

lazy val root = project.in(file("."))
  .settings(libraryDependencies ++= Seq(
    "io.gatling" % "gatling-test-framework" % gatlingVersion % "provided,test" exclude("org.asynchttpclient", "async-http-client"),
    "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % "provided,test" exclude("org.asynchttpclient", "async-http-client"),

    "com.trueaccord.scalapb" %% "scalapb-runtime" % scalapbVersion % "protobuf",
    "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % scalapbVersion,
    "io.grpc" % "grpc-netty" % "1.11.0",

  ))
  .enablePlugins(GatlingPlugin)


PB.targets in Compile := Seq(
  PB.gens.java -> (sourceManaged in Compile).value,
  scalapb.gen(javaConversions = true) -> (sourceManaged in Compile).value
)

inConfig(Test)(sbtprotoc.ProtocPlugin.protobufConfigSettings)

// Gatling contains scala-library
assemblyOption in assembly := (assemblyOption in assembly).value
  .copy(includeScala = false)

assemblyMergeStrategy in assembly := {
  case x if x.endsWith("io.netty.versions.properties") => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

resolvers ++= Seq(
  Resolver.mavenLocal,
  Resolver.mavenCentral,
  Resolver.sonatypeRepo("releases")
)
