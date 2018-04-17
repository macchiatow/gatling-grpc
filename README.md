# Gatling-GRPC [![Build Status](https://travis-ci.org/macchiatow/gatling-grpc.svg?branch=master)](https://github.com/macchiatow/gatling-grpc)

An unofficial [Gatling](http://gatling.io/) 2.3.1 stress test plugin
for [gRPC](https://grpc.io) 1.11.0 protocol.

## Usage

### Cloning this repository

    $ git clone https://github.com/macchiatow/gatling-grpc.git
    $ cd gatling-grpc

### Creating a jar file

Install [sbt](http://www.scala-sbt.org/) 1.0 if you don't have.
And create a jar file:

    $ sbt assembly

If you want to change the version of Gatling used to create a jar file,
change the following line in [`build.sbt`](build.sbt):

```scala
lazy val gatlingVersion = "2.3.1"
```

### Putting the jar file to lib directory

Put the jar file to `lib` directory in Gatling:

    $ cp target/scala-2.12/gatling-grpc-assembly-*.jar /path/to/gatling-charts-highcharts-bundle-2.2.*/lib

###  Creating a simulation file

    $ cd /path/to/gatling-charts-highcharts-bundle-2.2.*
    $ vi user-files/simulations/BasicGrpcSimulation.scala

You can find sample simulation files in the [test directory](src/test/scala/me/macchiatow/gatling/grpc).

### Running a stress test

run a stress test via shell executor:

    $ bin/gatling.sh
    
or via gatling-sbt plugin
    
    $ sbt gatling:test

## License

Apache License, Version 2.0