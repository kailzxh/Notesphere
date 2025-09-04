# Use official OpenJDK 21 as base
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (to cache dependencies)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Give execute permission to mvnw
RUN chmod +x mvnw

# Download dependencies (cache this layer)
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the source code
COPY src ./src

# Package the application
RUN ./mvnw clean package -DskipTests

# Expose port (must match your Spring Boot server.port)
EXPOSE 8080

# Run the jar file
CMD ["java", "-jar", "target/notesapp-backend-1.0.0.jar"]
