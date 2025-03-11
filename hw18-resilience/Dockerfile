# Build stage
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app

## Download and cache dependencies in maven local repository
COPY pom.xml .
RUN mvn dependency:go-offline

## Package jar file
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM bellsoft/liberica-openjdk-alpine-musl:17
WORKDIR /app
COPY --from=build /app/target/hw18-resilience-1.0.0.jar app.jar
RUN adduser --disabled-password non_root_user
USER non_root_user
EXPOSE 80
EXPOSE 90
ENTRYPOINT ["java", "-jar", "app.jar"]