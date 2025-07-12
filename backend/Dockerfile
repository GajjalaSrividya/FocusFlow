# ---------- Stage 1: Build the app ----------
FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /build
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ---------- Stage 2: Run the app ----------
FROM eclipse-temurin:17-jdk

# Required for waiting for Cassandra
RUN apt-get update && apt-get install -y netcat-openbsd

WORKDIR /app

# Copy the jar from the builder
COPY --from=builder /build/target/*.jar app.jar

# Copy the application.properties
COPY src/main/resources/application.properties ./application.properties

# Copy and make the wait script executable
COPY wait-for-cassandra.sh .
RUN chmod +x ./wait-for-cassandra.sh

# Entrypoint waits for Cassandra and then starts the Spring Boot app
ENTRYPOINT ["./wait-for-cassandra.sh", "java", "-jar", "app.jar", "--spring.config.location=classpath:/,file:./application.properties"]
