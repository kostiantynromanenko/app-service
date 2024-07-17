FROM gradle:jdk17 as gradleimage
COPY . /home/gradle/source
WORKDIR /home/gradle/source
RUN gradle build -x test --no-daemon

FROM amazoncorretto:17.0.11-alpine3.19
COPY --from=gradleimage /home/gradle/source/build/libs/app-service-*.jar /app/app.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]