FROM openjdk:17-jdk-alpine
COPY ./target/*.jar countries-app.jar
ENTRYPOINT ["java","-jar","countries-app.jar"]