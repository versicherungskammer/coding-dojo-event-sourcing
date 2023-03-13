ARG DOCKER_REPO
FROM ${DOCKER_REPO:-docker.io}/eclipse-temurin:11-jdk-focal AS builder

RUN mkdir -p /workspace

WORKDIR /workspace

COPY . /workspace

RUN ./gradlew --no-daemon assemble

FROM ${DOCKER_REPO:-docker.io}/eclipse-temurin:11-jre-focal

COPY --from=builder /workspace/build/libs/*-all.jar /application.jar

ENV KAFKA_BOOTSTRAP_SERVERS ""
ENV REPLAY_MODE "false"

WORKDIR /
ENTRYPOINT ["java"]
CMD ["-jar", "application.jar"]
