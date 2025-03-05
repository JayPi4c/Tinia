FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /opt/app
COPY . .
RUN ./mvnw -P docker package -B -pl core -am


FROM eclipse-temurin:21-jre-alpine
WORKDIR /opt/app

# Create a group and user
RUN addgroup -S tinia && adduser -S -G tinia tinia

COPY --from=builder /opt/app/core/target/tinia-core.jar .

# Change to 'tinia' user
USER tinia

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/opt/app/tinia-core.jar"]