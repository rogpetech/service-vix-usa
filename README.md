# Service-VIX-USA

## About the Project
Service-VIX-USA is a Java application built with Maven and Spring Boot. It leverages several libraries and frameworks, including Spring Security, Hibernate, Jackson, among others.

## Current Version
1.0.0

## Requirements
- Java 17
- Maven 3.9.8
- Docker
  
## Important Libraries and Tools
### [Thymeleaf](https://www.thymeleaf.org/)
Thymeleaf is a modern server-side Java template engine for web and standalone environments. It is used for generating HTML/XHTML/XML content. It provides a natural templating approach and integrates seamlessly with Spring.

### Lombok
Lombok is a Java library that helps to reduce boilerplate code by generating commonly used methods like getters, setters, and constructors using annotations. It simplifies the development process and makes the code more readable.

### JWT (JSON Web Tokens)
JWT is a compact, URL-safe means of representing claims to be transferred between two parties. It is commonly used for authorization and information exchange in web applications. Understanding JWT is crucial for implementing secure authentication mechanisms.

### jQuery
jQuery is a fast, small, and feature-rich JavaScript library. It simplifies tasks such as HTML document traversal and manipulation, event handling, and animation. In this project, it is used to enhance the interactivity of web pages.

### `pom.xml`
The `pom.xml` file is the core of a project's configuration in Maven. It contains information about the project and configuration details used by Maven to build the project. It includes dependencies, build plugins, and other project-related settings.

### `Procfile`
A `Procfile` is a text file in the root directory of your application that explicitly declares what command should be executed to start your app. It is used by Heroku and other platforms for deployment. It typically contains the command to run the application.

### `.env`
The `.env` file is used to store environment variables, which can be used to configure various aspects of the application without hardcoding them into the codebase. It allows for better management of configuration settings across different environments.

### `system.properties`
The `system.properties` file is used to specify the Java version to be used in your application. For example:

```properties
java.runtime.version=17
```

### Folder Structure Explanation
- **config**: Contains configuration classes and files for the application. These might include security configurations, CORS configurations, or any other global settings.
- **controller**: Houses the controller classes that handle incoming HTTP requests and route them to appropriate service methods.
- **dto**: Data Transfer Objects are used to transfer data between different layers of the application.
- **enums**: Contains enumerations used across the application, providing a predefined set of constants.
- **filter**: Contains filter classes used for processing requests and responses, often used for tasks like logging, authentication, and authorization.
- **mapper**: Holds classes that map between different models or DTOs, often used in converting entities to DTOs and vice versa.
- **models**: Contains the domain models or entities that represent the data structure of the application.
- **repositories**: Contains repository interfaces that extend Spring Data JPA to interact with the database.
- **scheduler**: Houses classes related to scheduling tasks that need to be executed periodically.
- **service**: Contains service classes where the business logic resides. These classes are called by the controllers to perform specific tasks.
- **utility**: Holds utility or helper classes that provide common functionality used across different parts of the application.
- **ServiceVixApplication.java**: The main class of the Spring Boot application, containing the `main` method to run the application.


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

4. Open browser:

```sh
open http://localhost:8081
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
│   │   │       └── service
│   │   │           └── vix
│   │   │               └── config
│   │   │               └── controller
│   │   │               └── dto
│   │   │               └── enums
│   │   │               └── filter
│   │   │               └── mapper
│   │   │               └── models
│   │   │               └── repositories
│   │   │               └── scheduler
│   │   │               └── service
│   │   │               └── utility
│   │   │               └── ServiceVixApplication.java
│   │   └── resources
│   │   │   └── application.yml
│   │   │   └── templates
│   │   │       └── invoice
│   │   │           └── invoices.html 
│   │   └── webapp
│   │       └── application.properties
│   └── test
│       └── java
│           └── com
│               └── service
│                   └── vix
│                       └── models
│                       └── ServiceVixApplicationTests.java
├── Dockerfile
├── pom.xml
|── .env
|── Procfile
└── README.md
```

## Configuration
### application.properties
Configure your application properties in the `src/main/resources/application.properties` file. Here you can set up your database configurations, server port, and other necessary settings.

## Contributing
We follow [GitFlow](https://www.atlassian.com/br/git/tutorials/comparing-workflows/gitflow-workflow#:~:text=O%20que%20%C3%A9%20Gitflow%3F,por%20Vincent%20Driessen%20no%20nvie.) and use Jira for project management. Please follow the guidelines below when contributing:

1. Clone the Project
2. Create your Feature Branch (git checkout -b feature/JIRA-ISSUE-CODE)
3. Commit your Changes (git commit -m 'Add some AmazingFeature')
4. Push to the Branch (git push origin feature/JIRA-ISSUE-CODE)
5. Open a Pull Request
6. Waiting for Code Review and Approve Pull Request
7. Use git checkout develop and git pull origin develop
8. Use git checkout -b release/JIRA-ISSUE-CODE
9. Continue the flow with gitflow

During development, please ensure to document user-facing changes and update the relevant documentation.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

Project Link: [https://github.com/rogpetech/service-vix-usa](https://github.com/rogpetech/service-vix-usa)
