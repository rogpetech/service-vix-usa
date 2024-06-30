# Use uma imagem base do Java
FROM openjdk:17-jdk-alpine

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo JAR para o container
COPY target/servicevix.jar /app/app.jar

# Expõe a porta que a aplicação vai usar
EXPOSE 8080

# Define o comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
