FROM eclipse-temurin:21-jre-alpine

WORKDIR /bank-app
COPY notifications/target/*.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]