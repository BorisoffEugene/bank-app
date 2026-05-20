FROM eclipse-temurin:21-jre-alpine

WORKDIR /transfer
COPY target/*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]