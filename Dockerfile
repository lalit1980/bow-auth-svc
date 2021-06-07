# Required for starting application up.
#FROM openjdk:8-jdk-alpine
FROM alpine:latest
ENV JAVA_HOME="/usr/lib/jvm/default-jvm/"
ENV PATH=$PATH:${JAVA_HOME}/bin
RUN apk add openjdk11
COPY target/bow-auth-svc-release.jar app.jar
EXPOSE 9010

ENTRYPOINT ["java","-Dspring.profiles.active=kubernetes","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]