FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY target/FoodBankingBackend-0.0.1-SNAPSHOT.jar.original /app/foodbankingapi.jar

EXPOSE 8080

CMD ["java", "-jar", "foodbankingapi.jar", "--spring.main.main-class=com.bezkoder.springjwt.FoodBackingBackendApplication"]