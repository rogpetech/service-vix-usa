# Service-VIX-USA

## About the Project
Service-VIX-USA is a Java application built with Maven and Spring Boot. It leverages several libraries and frameworks, including Spring Security, Hibernate, Jackson, among others.

## Current Version
1.0.0

## Requirements
- Java 17
- Maven 3.9.8
- Docker

## Features
- Authentication and authorization using Spring Security
- Data persistence with Hibernate
- JSON serialization and deserialization with Jackson
- Docker support for easy deployment

## Getting Started

### Using Docker
1. Build the Docker image:
    ```sh
    docker build -t service-vix-usa .
    ```
2. Run the Docker container:
    ```sh
    docker run -p 8080:8080 service-vix-usa
    ```

### Using Maven
1. Compile the project:
    ```sh
    mvn clean package -DskipTests
    ```
2. Run the application:
    ```sh
    java -jar target/app.jar
    ```
3. Run the server:
  ```sh
   mvn spring-boot:run
  ```

## Running Tests
To run the tests, use the following command:
```sh
mvn test
```

## Project Structure
```plaintext
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── servicevixusa
│   │   │               └── [your Java classes]
│   │   └── resources
│   │       └── application.properties
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── servicevixusa
│                       └── [your test classes]
├── Dockerfile
├── pom.xml
└── README.md
```

## Configuration
### application.properties
Configure your application properties in the `src/main/resources/application.properties` file. Here you can set up your database configurations, server port, and other necessary settings.

## Contributing
Contributions are welcome! Please clone the repository and create a pull request with your changes.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

Project Link: [https://github.com/rogpetech/service-vix-usa](https://github.com/rogpetech/service-vix-usa)
