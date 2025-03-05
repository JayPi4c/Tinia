FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /opt/app
COPY . .
RUN mvn -P pipeline package -B -pl pipeline -am

FROM eclipse-temurin:21-jre-alpine
WORKDIR /opt/app

# Create a group and user
RUN addgroup -S tinia && adduser -S -G tinia tinia

COPY --from=builder /opt/app/pipeline/target/tinia-pipeline.jar .

# Change to 'tinia' user
USER tinia

ENTRYPOINT ["java", "-jar", "/opt/app/tinia-pipeline.jar"]