FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
COPY src /home/sample-kafka-producer/src
COPY pom.xml /home/sample-kafka-producer
RUN mvn -f /home/sample-kafka-producer/pom.xml clean package -Dmaven.test.skip

FROM eclipse-temurin:21
MAINTAINER abhishek
COPY --from=build /home/sample-kafka-producer/target/sample-kafka-producer-0.0.1-SNAPSHOT.jar sample-kafka-producer-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/sample-kafka-producer-0.0.1-SNAPSHOT.jar"]