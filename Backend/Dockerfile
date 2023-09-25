FROM openjdk:11-jre-slim
FROM maven:3.8.7
VOLUME /tmp
COPY target/*.jar project.jar
ENTRYPOINT ["java","-jar","/project.jar"]