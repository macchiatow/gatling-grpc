FROM openjdk:8-jdk-alpine

ENV GATLING_VERSION 2.3.1
ENV SCALA_VERSION 2.12

WORKDIR /opt

RUN wget https://repo1.maven.org/maven2/io/gatling/highcharts/gatling-charts-highcharts-bundle/${GATLING_VERSION}/gatling-charts-highcharts-bundle-${GATLING_VERSION}-bundle.zip
RUN unzip gatling-charts-highcharts-bundle-${GATLING_VERSION}-bundle.zip
RUN mv gatling-charts-highcharts-bundle-${GATLING_VERSION} gatling

WORKDIR  /opt/gatling

COPY target/scala-${SCALA_VERSION}/gatling-grpc-assembly-*.jar ./lib
COPY test-spec/target/scala-${SCALA_VERSION}/test-fund-service-assembly-*.jar ./lib
COPY src/test/scala/me/macchiatow/gatling/grpc/BasicGrpcSimulation.scala ./user-files/simulations

# thess rm only required because libs from test-fund-service-assembly-*.jar conflict with gatling provided
# when you use gatling-grpc-assembly-*.jar without mocked gRPC service from test-fund-service, no error should occur
RUN rm ./lib/netty-*
RUN rm ./lib/async-http-client*
RUN rm -rf ./user-files/simulations/computerdatabase

ENTRYPOINT ["sh", "/opt/gatling/bin/gatling.sh"]