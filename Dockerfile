#FROM openjdk:8-jdk-alpine
#
#WORKDIR /app
#
#COPY target/FoodBankingBackend-0.0.1-SNAPSHOT.jar.original /app/foodbankingapi.jar
#
#EXPOSE 8080
#
#CMD ["java", "-jar", "foodbankingapi.jar", "--spring.main.main-class=com.bezkoder.springjwt.FoodBackingBackendApplication"]

# Stage 1: Build Stage
FROM maven:3.8.4-jdk-8 AS build
WORKDIR /app

# Copy only the pom.xml to cache dependencies
COPY pom.xml .

# Download dependencies and cache them (if pom.xml has changed)
RUN mvn dependency:go-offline

# Copy the rest of the source code and build the application
COPY src/ /app/src/
RUN mvn package -DskipTests

# Stage 2: Run Stage
FROM openjdk:8-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/foodbankingapi.jar ./

# Expose the port your application runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "foodbankingapi.jar"]