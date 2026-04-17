# ── Stage 1: Compilation ──────────────────────────────
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw

RUN ./mvnw install -DskipTests > /dev/null 2>&1 || true

COPY src ./src

RUN ./mvnw package -DskipTests

# ── Stage 2: Runtime ──────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
