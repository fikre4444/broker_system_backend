# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
# Copy the .env file to the build context
COPY .env /app/.env
COPY pom.xml .
COPY src ./src
RUN mvn clean install

# Stage 2: Run the application
FROM openjdk:17-alpine
WORKDIR /app
# Copy the .env file for runtime
COPY .env /app/.env
COPY --from=build /app/target/axumawit-0.0.1-SNAPSHOT.jar ./axumawit-aws.jar
EXPOSE 8080
CMD ["java", "-jar", "axumawit-aws.jar"]