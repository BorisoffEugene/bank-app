FROM eclipse-temurin:21-jre-alpine

WORKDIR /bank-app
COPY cash/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]