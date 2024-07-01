# Fase de construção
FROM maven:3.8.4-openjdk-17 AS builder

# Instalar o Git usando apk (Alpine Linux)
RUN apk update && apk add git

# Define o diretório de trabalho no container
WORKDIR /app

# Copia os arquivos pom.xml e src para o diretório de trabalho
COPY pom.xml .
COPY src ./src

# Constrói a aplicação utilizando o Maven
RUN mvn clean package -DskipTests

# Fase de execução
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho no container
WORKDIR /app

# Copia o arquivo JAR da fase de construção
COPY --from=builder /app/target/ServiceVix-0.0.1-SNAPSHOT.jar /app/ServiceVix-0.0.1-SNAPSHOT.jar

# Expõe a porta 8080 para o mundo exterior
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "/app/ServiceVix-0.0.1-SNAPSHOT.jar"]
