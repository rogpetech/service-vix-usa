# Fase de construção
FROM maven:3.9.8-eclipse-temurin-17 AS builder

# Define o diretório de trabalho no container
WORKDIR /app

# Copia o arquivo pom.xml e o diretório src para o diretório de trabalho
COPY pom.xml .
COPY src ./src

# Constrói a aplicação utilizando o Maven
RUN mvn clean package -DskipTests

# Fase de execução
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho no container
WORKDIR /app

# Copia o arquivo JAR da fase de construção
COPY --from=builder /app/target/*.jar /app/app.jar
RUN chmod +r /app/app.jar
RUN chmod -R +r /app/static

# Expor a porta que a aplicação vai rodar
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "/app/app.jar"]
