FROM maven:3.9-eclipse-temurin-25 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
RUN apk add --no-cache curl
COPY --from=builder /app/target/messenger-1.0.0.jar app.jar
ENV JAVA_OPTS="-XX:+UseZGC -XX:ZCollectionInterval=30 -Xmx1024m -Xms64m"
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:25565/ping || exit 1
EXPOSE 25565
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]