FROM maven:3.8.7-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src/ /app/src/
COPY keys/ /app/keys/
COPY config/application.yaml /app/config/application.yaml
RUN sed -i 's/localhost:5432/dsp-postgres:5432/g' /app/config/application.yaml
RUN mvn package -DskipTests

FROM amazoncorretto:17-alpine3.17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar /app/dsp.jar
COPY --from=build /app/config/application.yaml /app/application.yaml
COPY --from=build /app/keys/ /app/keys/
ENTRYPOINT ["java", "-jar", "dsp.jar", "--spring.config.location=file:application.yaml"]
