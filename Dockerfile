
FROM azul/zulu-openjdk-alpine:21

WORKDIR /app

COPY target/blogpostapi.jar blogpostapi.jar

EXPOSE 8080

CMD ["java", "-jar", "blogpostapi.jar"]