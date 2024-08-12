FROM openjdk:21-jdk-oracle
WORKDIR /app
COPY target/ControlPanel-0.0.1-SNAPSHOT.jar /app/ControlPanel.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ControlPanel.jar"]
