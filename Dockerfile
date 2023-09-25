FROM openjdk:17.0.2-slim-bullseye
COPY target/pipeline.jar pipeline.jar
ENTRYPOINT ["java","-jar","/pipeline.jar"]