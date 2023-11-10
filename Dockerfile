
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/blogpostapi.jar blogpostapi.jar

EXPOSE 8080

CMD ["java", "-jar", "blogpostapi.jar"]