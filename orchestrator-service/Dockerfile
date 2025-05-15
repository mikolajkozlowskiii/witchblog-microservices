FROM maven:3-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/orchestrator-service.jar ./orchestrator-service.jar
CMD ["java", "-jar", "orchestrator-service.jar"]
