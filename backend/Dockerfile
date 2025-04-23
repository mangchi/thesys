FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app


# Copy Gradle wrapper files
COPY gradlew .
COPY gradle gradle

# Copy build files
COPY build.gradle settings.gradle ./

# Ensure gradlew has execute permissions and pre-download dependencies (cache optimization)
RUN chmod +x ./gradlew && ./gradlew build -x test || return 0

# Copy the entire source code
COPY . .

# Build the project
RUN ./gradlew build -x test

# Run the application
CMD ["java", "-jar", "build/libs/app.jar"]