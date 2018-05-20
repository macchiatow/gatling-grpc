addSbtPlugin("io.gatling" % "gatling-sbt" % "2.2.2")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.6")
addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.13")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.1")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.0")
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.1")
addSbtPlugin("com.geirsson" % "sbt-scalafmt" % "1.5.1")

resolvers += "Bintray sbt plugin releases" at "http://dl.bintray.com/sbt/sbt-plugin-releases/"

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.7.4"
