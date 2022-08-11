# https://spring.io/guides/gs/spring-boot-docker/
FROM openjdk:17-alpine
ARG JAR_FILE=target/ecinema-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]