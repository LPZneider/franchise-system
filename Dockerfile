# Multi-stage build
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Maven wrapper and pom.xml first for better layer caching
COPY .mvn ./.mvn
COPY mvnw .
COPY pom.xml .

# Make mvnw executable
RUN chmod +x ./mvnw

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Production stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create logs directory
RUN mkdir ./logs

# Copy the built jar from builder stage
COPY --from=builder /app/target/franchise-system-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8001

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8001/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
