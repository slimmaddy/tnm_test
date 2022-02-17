FROM openjdk:8-jdk-alpine AS MAVEN_BUILD

MAINTAINER MinhDo

COPY pom.xml /build/
COPY src /build/src/
COPY mvnw /build/
COPY .mvn /build/.mvn/

WORKDIR /build/
RUN ./mvnw package

FROM openjdk:8-jdk-alpine
WORKDIR /app
VOLUME ["/logs"]
EXPOSE 8080
COPY --from=MAVEN_BUILD /build/target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]