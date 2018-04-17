addSbtPlugin("io.gatling" % "gatling-sbt" % "2.2.2")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.6")
addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.13")
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.1")

resolvers += "Bintray sbt plugin releases" at "http://dl.bintray.com/sbt/sbt-plugin-releases/"

libraryDependencies += "com.trueaccord.scalapb" %% "compilerplugin" % "0.6.7"