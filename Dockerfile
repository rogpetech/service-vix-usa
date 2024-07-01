# Use a imagem oficial do Maven para construir a aplicação
FROM maven:3.8.4-openjdk-17 AS builder

# Define o diretório de trabalho no container
WORKDIR /app

# Copia o arquivo pom.xml e a pasta src para o diretório de trabalho
COPY pom.xml .
COPY src ./src

# Constrói a aplicação utilizando o Maven
RUN mvn clean package -DskipTests

# Usa a imagem oficial do OpenJDK para rodar a aplicação
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho no container
WORKDIR /app

# Copia o arquivo JAR da fase de construção
COPY --from=builder /app/target/ServiceVix-0.0.1-SNAPSHOT.jar /app/ServiceVix-0.0.1-SNAPSHOT.jar

# Expõe a porta 8080 para o mundo exterior
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "/app/ServiceVix-0.0.1-SNAPSHOT.jar"]
