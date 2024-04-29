# Stage 1: Build the application
FROM openjdk:17 AS build
WORKDIR /home/app
COPY . .
RUN ./gradlew bootJar --no-daemon --stacktrace --info

# Stage 2: Run the application
FROM openjdk:17-slim-buster
EXPOSE 8080
COPY --from=build /home/app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]