FROM openjdk:21-jdk-slim
COPY target/*.jar app.jar
EXPOSE 25565
ENTRYPOINT ["java", "-jar", "/app.jar"]