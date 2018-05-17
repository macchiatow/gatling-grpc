scalaVersion := "2.12.4"
parallelExecution in Test := false

lazy val gatlingVersion = "2.3.1"

val commonSettings = Seq(
  name := "gatling-grpc",
  organization := "me.macchiatow",
  version := gatlingVersion
)

import scalapb.compiler.Version.scalapbVersion

lazy val root = project.in(file("."))
  .settings(pbSettings, commonSettings, grpcSettings, assemblySettings)
  .settings(
    libraryDependencies ++= Seq(
      "io.gatling" % "gatling-test-framework" % gatlingVersion % "provided,test"  exclude("org.asynchttpclient", "async-http-client"),
      "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % "provided,test"  exclude("org.asynchttpclient", "async-http-client"),
    ))
  .enablePlugins(GatlingPlugin)

val grpcSettings = Seq(
  libraryDependencies ++= Seq(
    "io.grpc" % "grpc-netty" % "1.11.0"
  )
)

val pbSettings = Seq(
  libraryDependencies ++= Seq(
    "com.thesamet.scalapb" %% "scalapb-runtime" % scalapbVersion % "protobuf",
    "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapbVersion
  ),
  PB.targets in Compile := Seq(
    scalapb.gen(flatPackage = true) -> (sourceManaged in Compile).value
  )
)

val assemblySettings = Seq(
  assemblyMergeStrategy in assembly := {
    case x if x.endsWith("io.netty.versions.properties") => MergeStrategy.first
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)

// Gatling contains scala-library
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

resolvers ++= Seq(
  Resolver.mavenLocal,
  Resolver.mavenCentral,
  Resolver.sonatypeRepo("releases")
)
