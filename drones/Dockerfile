FROM openjdk:17-jdk-alpine

WORKDIR /drones

COPY target/drones-0.0.1-SNAPSHOT.jar drones-0.0.1-SNAPSHOT.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "drones-0.0.1-SNAPSHOT.jar"]