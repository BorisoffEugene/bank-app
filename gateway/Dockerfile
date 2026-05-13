FROM eclipse-temurin:21-jre-alpine

WORKDIR /bank-app
COPY gateway/target/*.jar app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]