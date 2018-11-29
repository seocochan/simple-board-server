FROM openjdk:8-jre

COPY ./target/simple-board-server.jar app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "app.jar"]