# 1. Use an official, lightweight Java 17 environment as our foundation
FROM eclipse-temurin:17-jdk-alpine

# 2. Create a folder inside the container to hold our app
WORKDIR /app

# 3. Copy the compiled .jar file from your laptop's target folder into the container
COPY target/*.jar app.jar

# 4. Expose port 8080 so the internet can access the dashboard
EXPOSE 8080

# 5. The exact command to boot up the server
ENTRYPOINT ["java", "-jar", "app.jar"]