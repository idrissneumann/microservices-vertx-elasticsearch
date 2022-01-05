ARG MAVEN_VERSION=3.3-jdk-8
ARG TOMCAT_VERSION=9.0-jre8-slim
ARG OPENJDK_VERSION=8-jre-slim

##############################################################
# Stage vertx_service_build: build maven for vert.x base app #
##############################################################

FROM maven:${MAVEN_VERSION} AS vertx_service_build

ARG IMAGE_SERVICE
ENV BASE_DIR="/uprodit"
ARG MVN_ARTIFACT_VERSION=0.0.1-SNAPSHOT

WORKDIR ${BASE_DIR}

COPY . ${BASE_DIR}
COPY ./prodit-batch/src/main/resources/env/docker/vertx/log4j2.xml /

RUN mv /log4j2.xml /uprodit/prodit-${IMAGE_SERVICE}/src/main/resources && \
    mvn clean install -Dmaven.test.skip -Djacoco.skip=true -Dcheckstyle.skip=true -Dwadl.skip=true -P ${IMAGE_SERVICE} && \
    mv /uprodit/prodit-${IMAGE_SERVICE}/target/prodit-${IMAGE_SERVICE}-${MVN_ARTIFACT_VERSION}-fat.jar /vertx-fat.jar && \
    mv manifest.json /manifest.json

###################################################
# Stage vertx_service: exposing a vertx based app #
###################################################

FROM openjdk:${OPENJDK_VERSION} AS vertx_service

ENV VERTX_PORT=80

COPY --from=vertx_service_build /vertx-fat.jar /manifest.json /

EXPOSE 80
CMD ["java", "-jar", "/vertx-fat.jar"]
