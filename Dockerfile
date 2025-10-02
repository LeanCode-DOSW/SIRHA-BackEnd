#importar una base
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
# dependencias
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline
# compilar
COPY src ./src
RUN mvn -B -q -DskipTests package

# runtime 
FROM eclipse-temurin:17-jre
WORKDIR /app
# usuario no root
RUN useradd -ms /bin/bash appuser
COPY --from=builder /app/target/*.jar app.jar

# Puerto de Spring
EXPOSE 8080

# Variables comunes (ajusta SPRING_PROFILES_ACTIVE si quieres)
ENV JAVA_OPTS="" \
    SPRING_PROFILES_ACTIVE=prod

# Healthcheck a actuator
HEALTHCHECK --interval=30s --timeout=5s --retries=5 CMD wget -qO- http://localhost:8080/actuator/health || exit 1

USER appuser
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
