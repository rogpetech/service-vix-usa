# Use the official Maven image to create a build artifact.
# This stage is named 'builder' and is used to build the application.
FROM maven:3.8.4-openjdk-17-slim AS builder

# Set the working directory inside the container.
WORKDIR /app

# Copy the project files into the container.
COPY pom.xml .
COPY src ./src

# Package the application using Maven.
RUN mvn clean package -DskipTests

# Use the official OpenJDK image to run the application.
FROM openjdk:17-jdk-slim

# Set the working directory inside the container.
WORKDIR /app

# Copy the jar file from the 'builder' stage.
COPY --from=builder /app/target/ServiceVix-0.0.1-SNAPSHOT.jar /app/ServiceVix-0.0.1-SNAPSHOT.jar

# Expose port 8080 to the outside world.
EXPOSE 8080

# Command to run the application.
CMD ["java", "-jar", "/app/ServiceVix-0.0.1-SNAPSHOT.jar"]
