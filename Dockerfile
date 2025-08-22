# Step 1: Use Maven to build the app
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy pom.xml and download dependencies (caches better)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Run the app with JDK
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy built jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port (must match Render’s port env variable)
EXPOSE 8080

# Run Spring Boot
ENTRYPOINT ["java","-jar","app.jar"]
