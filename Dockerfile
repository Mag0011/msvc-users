FROM openjdk:11-ea-17-jdk
WORKDIR /app
COPY ./target/msvc-users-0.0.1-SNAPSHOT.jar .
EXPOSE 8001
ENTRYPOINT ["java", "-jar", "msvc-users-0.0.1-SNAPSHOT.jar"]