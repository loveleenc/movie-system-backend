FROM maven:3.9.11-amazoncorretto-17-alpine@sha256:f6b7c9f1c635054a653a6a4eaec6d4a870532cd7cbaec6243772a5b64960a978 AS build
RUN mkdir /project
COPY . /project
WORKDIR /project
RUN mvn clean package -Dmaven.test.skip=true

FROM maven:3.9.11-amazoncorretto-17-alpine@sha256:f6b7c9f1c635054a653a6a4eaec6d4a870532cd7cbaec6243772a5b64960a978
RUN mkdir -p project
RUN mkdir -p project/secrets

COPY --from=build /project/application/target/*.jar /project/java-app.jar
COPY --from=build /project/setup.sh /project/setup.sh
WORKDIR /project
RUN chmod +x ./setup.sh
CMD ["./setup.sh"]