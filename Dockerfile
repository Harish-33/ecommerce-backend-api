# Multi-stage Dockerfile for Spring Boot Application

# Stage 1: Build stage
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /workspace/app

# Copy maven wrapper and pom.xml first to take advantage of Docker layer caching
COPY pom.xml .
# Copy maven source files
COPY src src

# Install maven and package application
RUN apt-get update && apt-get install -y maven && \
    mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Run as non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Copy built jar from builder stage
COPY --from=builder --chown=spring:spring /workspace/app/target/*.jar app.jar

# Expose standard application port
EXPOSE 8080

# Configure JVM options for container awareness
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
