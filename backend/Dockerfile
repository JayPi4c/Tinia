FROM eclipse-temurin:25-jdk-alpine-3.23 AS builder
WORKDIR /opt/app
COPY . .
RUN ./mvnw -P pipeline package -B -pl pipeline -am

FROM eclipse-temurin:25-jre-alpine-3.23
WORKDIR /opt/app

# Create a group and user
RUN addgroup -S tinia && adduser -S -G tinia tinia

COPY --from=builder /opt/app/pipeline/target/tinia-pipeline.jar .

# Change to 'tinia' user
USER tinia

ENTRYPOINT ["java", "-jar", "/opt/app/tinia-pipeline.jar"]