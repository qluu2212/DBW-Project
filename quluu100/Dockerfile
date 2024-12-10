# syntax=docker/dockerfile:1
ARG JAVA_VERSION=21

FROM eclipse-temurin:${JAVA_VERSION}-jdk AS base
USER root
WORKDIR /root/submission

FROM gradle:jdk${JAVA_VERSION} AS build
USER root
WORKDIR /root/submission
RUN ["mkdir", "-p", ".gradle/caches-ro"]
ENV GRADLE_USER_HOME=/root/.gradle
ENV GRADLE_RO_DEP_CACHE=/root/.gradle/caches-ro
COPY . .
RUN ["gradle", ":bootJar"]

FROM base AS extract
COPY --from=build /root/submission/build/libs/implementation.jar implementation.jar
RUN ["java", "-Djarmode=layertools", "-jar", "implementation.jar", "extract"]

FROM base
COPY --from=extract /root/submission/dependencies/ ./
COPY --from=extract /root/submission/spring-boot-loader/ ./
COPY --from=extract /root/submission/snapshot-dependencies/ ./
COPY --from=extract /root/submission/application/ ./
COPY database.db /root/submission/database.db
HEALTHCHECK --interval=2m --start-period=1m --timeout=5s CMD ["curl", "--head", "-fsS", "localhost/actuator/health"]
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
