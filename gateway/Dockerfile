FROM eclipse-temurin:21-jre-alpine

WORKDIR /gateway
COPY target/*.jar app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]