# Use a Java 21 image
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Expose port 8080
EXPOSE 8080

# Run the jar
CMD ["java", "-jar", "target/notesapp-backend-1.0.0.jar"]
