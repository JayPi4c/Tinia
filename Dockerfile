FROM openjdk:17.0.2
COPY target/pipeline.jar pipeline.jar
ENTRYPOINT ["java","-jar","/pipeline.jar"]