# Use a base image for Java runtime
FROM eclipse-temurin:21-jdk-alpine AS builder

# Set working directory
WORKDIR /app

# Copy the Maven/Gradle configuration and application source code
COPY . .

# Build the application
RUN ./mvnw clean package -DskipTests

# Use a lightweight image for the runtime
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the built artifact from the builder stage
COPY --from=builder /app/target/ecommerce-*.jar /app/ecommerce.jar

# Expose the application's default port (adjust if needed)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/ecommerce.jar"]