FROM denvazh/gatling:2.3.1

COPY target/scala-2.12/gatling-grpc-assembly-*.jar /opt/gatling/lib
COPY test-spec/target/scala-2.12/test-fund-service-assembly-*.jar /opt/gatling/lib
COPY src/test/scala/me/macchiatow/gatling/grpc/BasicGrpcSimulation.scala /opt/gatling/user-files/simulations/

# netty already provided by test-fund-service
RUN rm /opt/gatling/lib/netty-*
RUN rm /opt/gatling/lib/async-http-client*
RUN rm -rf /opt/gatling/user-files/simulations/computerdatabase