FROM openjdk:17-jdk-alpine

WORKDIR /drones

COPY target/drones-0.0.1-SNAPSHOT.jar drones-0.0.1-SNAPSHOT.jar

EXPOSE 8888

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "drones-0.0.1-SNAPSHOT.jar"]