# Gatling-GRPC [![Build Status](https://travis-ci.org/macchiatow/gatling-grpc.svg?branch=master)](https://travis-ci.org/macchiatow/gatling-grpc)

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

### Running a stress test

#### Using Docker

- How to run a stress test within docker container?

        $ docker build -t gatling-grpc .
        $ docker run -it gatling-grpc:latest

- How to look test result?

        # firstly get container name
        $ docker ps -a | grep 'gatling-grpc:latest' | awk '{print $13}'
        # assume container name is romantic_hawking
        # secondly copy test result from container
        $docker cp romantic_hawking:/opt/gatling/results .

#### Using sbt
    
or via gatling-sbt plugin
    
    $ sbt gatling:test

## License

Apache License, Version 2.0
