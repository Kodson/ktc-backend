# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn

# Copy the source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Copy the built jar to the container
RUN cp target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8081

# Run the jar file
ENTRYPOINT ["java","-jar","/app/app.jar"]
