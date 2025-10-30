FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -B -q -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app

RUN useradd -ms /bin/bash appuser
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8443

ENV JAVA_OPTS="" \
    SPRING_PROFILES_ACTIVE=prod

HEALTHCHECK --interval=30s --timeout=5s --retries=5 \
  CMD curl -kfsS https://localhost:8443/actuator/health || exit 1

USER appuser
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
