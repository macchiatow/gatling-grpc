name := "gatling-grpc"
organization := "me.macchiatow"
version := "1.0.1"

scalaVersion := "2.12.4"
parallelExecution in Test := false


import com.trueaccord.scalapb.compiler.Version.scalapbVersion

lazy val root = project.in(file("."))
  .settings(libraryDependencies ++= Seq(
    "io.gatling" % "gatling-test-framework" % "2.3.1" exclude("org.asynchttpclient", "async-http-client"),
    "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.3.1" exclude("org.asynchttpclient", "async-http-client"),

    "com.trueaccord.scalapb" %% "scalapb-runtime" % scalapbVersion % "protobuf",
    "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % scalapbVersion,
    "io.grpc" % "grpc-netty" % "1.11.0",

  ))
  .enablePlugins(GatlingPlugin)
  .enablePlugins(PackPlugin)


PB.targets in Compile := Seq(
  PB.gens.java -> (sourceManaged in Compile).value,
  scalapb.gen(javaConversions = true) -> (sourceManaged in Compile).value
)

inConfig(Test)(sbtprotoc.ProtocPlugin.protobufConfigSettings)

resolvers ++= Seq(
  Resolver.mavenLocal,
  Resolver.mavenCentral,
  Resolver.sonatypeRepo("releases")
)
