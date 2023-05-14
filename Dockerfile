FROM maven:3.8.7-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src/ /app/src/
RUN mvn package -DskipTests

FROM amazoncorretto:17-alpine3.17-jdk
WORKDIR /app
COPY --from=build /app/pom.xml /app/pom.xml
COPY --from=build /app/target/*.jar /app/dsp.jar
ENTRYPOINT ["java", "-jar", "dsp.jar", "--spring.config.location=file:application.yaml"]
