# Etapa de construção
FROM maven:3.8.1-jdk-11 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package

# Etapa de execução
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/ServiceVix-0.0.1-SNAPSHOT.jar /app/ServiceVix-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/ServiceVix-0.0.1-SNAPSHOT.jar"]
