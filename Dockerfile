# Stage 1: Build
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Copy gradle files
COPY gradle/ gradle/
COPY gradlew .
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY gradle/libs.versions.toml gradle/libs.versions.toml

# Copy source code
COPY api/ api/
COPY core/ core/
COPY infra/ infra/
COPY app/ app/

RUN chmod +x gradlew

# Build the application
RUN ./gradlew :app:bootJar --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copy the built jar from the build stage
# The jar name is defined in app/build.gradle.kts as ticket-service.jar
COPY --from=build /app/app/build/libs/ticket-service.jar .

# Expose the service port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "ticket-service.jar"]
