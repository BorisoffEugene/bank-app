FROM eclipse-temurin:21-jre-alpine

WORKDIR /bank-app
COPY transfer/target/*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]