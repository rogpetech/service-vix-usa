# Etapa 1: Build da aplicação
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app

# Copia o arquivo de configuração do Maven
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

# Copia o código-fonte da aplicação
COPY src ./src

# Compila a aplicação
RUN ./mvnw clean package -DskipTests

# Etapa 2: Execução da aplicação
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# Exponha a porta usada pela aplicação (substitua 8080 pela porta correta, se necessário)
EXPOSE 8080
