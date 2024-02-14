FROM openjdk:17-alpine
COPY target/photo-album-app-0.0.1-SNAPSHOT.jar photo-album-app.jar
ENTRYPOINT ["java", "-jar", "photo-album-app.jar"]

