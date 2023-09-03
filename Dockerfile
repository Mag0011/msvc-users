FROM openjdk:11-ea-28-jdk as builder
WORKDIR /app/msvc-users
COPY .mvn ./.mvn
COPY ./mvnw .
COPY pom.xml .
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target
COPY ./src ./src
RUN ./mvnw clean package -DskipTests
FROM openjdk:11-ea-28-jdk
EXPOSE 8001
WORKDIR /app
COPY --from=builder /app/msvc-users/target/msvc-users-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "msvc-users-0.0.1-SNAPSHOT.jar"]