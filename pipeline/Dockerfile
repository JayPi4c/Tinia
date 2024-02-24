FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /opt/app
COPY . .
RUN mvn -P pipeline package -B -pl pipeline -am

FROM openjdk:21
WORKDIR /opt/app

COPY --from=builder /opt/app/pipeline/target/tinia-pipeline.jar .

ENTRYPOINT ["java", "-jar", "/opt/app/tinia-pipeline.jar"]