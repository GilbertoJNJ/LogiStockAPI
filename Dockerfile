# Dockerfile
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/LogiStockAPI-0.0.1-SNAPSHOT.jar /app/
EXPOSE 8080
CMD ["java", "-jar", "LogiStockAPI-0.0.1-SNAPSHOT.jar"]