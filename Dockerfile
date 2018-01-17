FROM stakater/oracle-jdk:8u152-alpine-3.7
MAINTAINER Aadil Nazir, aadil@aurorasolutions.io
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} mail-service.jar
ENTRYPOINT ["java","-jar","mail-service.jar"]