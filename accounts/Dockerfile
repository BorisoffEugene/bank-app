FROM eclipse-temurin:21-jre-alpine

WORKDIR /accounts
COPY target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]