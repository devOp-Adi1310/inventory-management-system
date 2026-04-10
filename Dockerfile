# ==========================================
# STAGE 1: Build the application
# ==========================================
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy the Maven wrapper files and your source code
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Give execution permission to the Maven wrapper and build the .jar file
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# ==========================================
# STAGE 2: Run the application
# ==========================================
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy ONLY the built .jar file from the previous stage (makes the final container super lightweight)
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]