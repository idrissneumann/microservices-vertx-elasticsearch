ARG MAVEN_VERSION=3.3-jdk-8
ARG TOMCAT_VERSION=9.0-jre8-slim
ARG OPENJDK_VERSION=8-jre-slim

##############################################################
# Stage vertx_service_build: build maven for vert.x base app #
##############################################################

FROM maven:${MAVEN_VERSION} AS vertx_service_build

ARG IMAGE_SERVICE
ENV BASE_DIR="/bblvertx"
ARG MVN_ARTIFACT_VERSION=0.0.1-SNAPSHOT

WORKDIR ${BASE_DIR}

COPY . ${BASE_DIR}

RUN mvn clean install -Dmaven.test.skip && \
    mv /bblvertx/elasticsearch-example/target/bblvertx-${MVN_ARTIFACT_VERSION}-fat.jar /bblvertx-fat.jar

###################################################
# Stage vertx_service: exposing a vertx based app #
###################################################

FROM openjdk:${OPENJDK_VERSION} AS vertx_service

ENV VERTX_PORT=80

COPY --from=vertx_service_build /bblvertx-fat.jar /

EXPOSE 80
CMD ["java", "-jar", "/bblvertx-fat.jar"]
