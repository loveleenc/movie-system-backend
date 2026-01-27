FROM maven:3.9.11-amazoncorretto-17-alpine@sha256:f6b7c9f1c635054a653a6a4eaec6d4a870532cd7cbaec6243772a5b64960a978
RUN mkdir project
RUN mkdir app
EXPOSE 8080
COPY . /project
WORKDIR /project
RUN mvn clean package -Dmaven.test.skip=true -Pstandalone

COPY /target/catalog-1.0-SNAPSHOT.jar /app/main.jar
RUN rm -rf /project
WORKDIR /app
CMD ["java", "-jar", "main.jar"]
