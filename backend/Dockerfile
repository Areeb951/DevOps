FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Create a new stage for runtime
FROM openjdk:17-jre-slim

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=0 /app/target/*.jar app.jar

# Expose the port
EXPOSE 8080

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]
