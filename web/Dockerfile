FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /opt/app
COPY . .
RUN mvn -P web package -B -pl web -am

FROM openjdk:21
WORKDIR /opt/app

COPY --from=builder /opt/app/web/target/tinia-web.jar .

ENTRYPOINT ["java", "-jar", "/opt/app/tinia-web.jar"]