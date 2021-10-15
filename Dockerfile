FROM adoptopenjdk/openjdk11:alpine-jre

COPY target/gibdd-0.0.1-SNAPSHOT.jar /gibdd-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "gibdd-0.0.1-SNAPSHOT.jar"]