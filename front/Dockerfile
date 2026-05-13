FROM eclipse-temurin:21-jre-alpine

WORKDIR /bank-app
COPY front/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]