# syntax=docker/dockerfile:1.6

FROM eclipse-temurin:17-jre-jammy AS runtime
WORKDIR /app

RUN useradd --system --create-home --uid 1001 spring

COPY target/*.jar /app/app.jar

ENV SERVER_PORT=8080
EXPOSE 8080

USER spring
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+ExitOnOutOfMemoryError", \
  "-jar", "/app/app.jar"]
