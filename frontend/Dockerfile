# Phase 1: Build
FROM eclipse-temurin:25-jdk-alpine AS build

# Set work directory
WORKDIR /app

COPY . .

# Ensure Maven wrapper is executable
RUN chmod +x ./mvnw

# Build the application
RUN ./mvnw clean package -pl frontend -am -DskipTests --activate-profiles docker

# Phase 2: Runtime
FROM eclipse-temurin:25-jre-alpine

# Set work directory
WORKDIR /app

# Create a group and user for better security
RUN addgroup -S tinia && \
    adduser -S -G tinia tinia && \
    chown -R tinia:tinia /app

# Copy the JAR from the build stage
COPY --from=build /app/frontend/target/frontend.jar frontend.jar

USER tinia

EXPOSE 8080


# Run the application
ENV SPRING_PROFILES_ACTIVE=docker
CMD java -jar frontend.jar