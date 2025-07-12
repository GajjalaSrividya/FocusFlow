# ---------- Stage 1: Build the app ----------
FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /build
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ---------- Stage 2: Run the app ----------
FROM eclipse-temurin:17-jdk

# Install dependencies and cqlsh
RUN apt-get update && \
    apt-get install -y netcat-openbsd python3-pip && \
    pip3 install cqlsh --break-system-packages && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy built jar and config
COPY --from=builder /build/target/*.jar app.jar
COPY src/main/resources/application.properties ./application.properties

# Copy the wait script
COPY wait-for-cassandra.sh .
RUN chmod +x wait-for-cassandra.sh

# Start Spring Boot after Cassandra keyspace is ready
ENTRYPOINT ["./wait-for-cassandra.sh", "java", "-jar", "app.jar", "--spring.config.location=classpath:/,file:./application.properties"]
