FROM maven:4.0.0-rc-5-amazoncorretto-17-al2023@sha256:68ab2a64c3d1c1910c2878789ce8e940d3172ff8bc8bc3b65199218f959117ad as build
EXPOSE 8080
RUN mkdir project
COPY . /project
WORKDIR /project
RUN mvn clean package -Dmaven.test.skip=true -Pstandalone

FROM maven:4.0.0-rc-5-amazoncorretto-17-al2023@sha256:68ab2a64c3d1c1910c2878789ce8e940d3172ff8bc8bc3b65199218f959117ad
RUN mkdir app
COPY --from=build /project/target/*.jar /app/main.jar
WORKDIR /app
CMD ["java", "-jar", "main.jar"]
