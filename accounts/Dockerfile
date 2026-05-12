FROM eclipse-temurin:21-jre-alpine

WORKDIR /bank-app
COPY accounts/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]