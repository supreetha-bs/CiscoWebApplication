# CiscoWebApplication
Web App consisting of REST APIs that is developed using Java and MongoDB.

## Required Applications:

- Java 8
- Spring boot 2.2.0.RELEASE
- MongoDB 4.2.1
- MongoDB Java driver 3.11.1
- Maven 3.6.2

## MongoDB

- You will need to update the default MongoDB URI `spring.data.mongodb.uri` in the `application.properties` file.

## Commands

- Start the server in a console with `mvn spring-boot:run`.
- If you add some Unit Tests, you would start them with `mvn clean test`.
- You can build the project with : `mvn clean package`.
- You can run the project with jar and the embedded Tomcat: `java -jar target/restapi-0.0.1-SNAPSHOT.jar`.

## Swagger
- Swagger is configured in this project in `SwaggerConfig.java`.
- The APIs can be seen at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

## Author
- Supreetha Somasundar
