# CiscoWebApplication
Web App consisting of REST APIs that is developed using Java and MongoDB.

## Required Applications:

- Java 8
- Spring boot 2.2.0.RELEASE
- MongoDB 4.2.1
- MongoDB Java driver 3.11.1
- Maven 3.6.2

## MongoDB

- MongoDB is hosted on Mongo Atlas.
- If you are going to use the mongodb on your local machine then you will need to update the default MongoDB URI `spring.data.mongodb.uri` in the `application.properties` file.

## Commands

- Start the server in a console with `mvn spring-boot:run`.
- If you add some Unit Tests, you would start them with `mvn clean test`.
- You can build the project with : `mvn clean package`.
- You can run the project with jar and the embedded Tomcat: `java -jar target/restapi-0.0.1-SNAPSHOT.jar`.
- If you are going to execute the APIs on your local machine, call the GET API for example `http://localhost:8080/api/objects/`.
- If you are going to execute the APIs on your production, call the GET API for example `http://ciscowebapplication-env.bippfa4rem.us-east-2.elasticbeanstalk.com/api/objects/`.

## Swagger
- Swagger is configured in this project in `SwaggerConfig.java`.
- The APIs can be seen at [http://ciscowebapplication-env.bippfa4rem.us-east-2.elasticbeanstalk.com/swagger-ui.html](http://ciscowebapplication-env.bippfa4rem.us-east-2.elasticbeanstalk.com/swagger-ui.html).
- Click on `Objects Controller` on the page to view the details of the API.

## Further Enhancements
1. Firstly, I would add authentication and authorization to the application. Currently it is accessible to everyone. I would add a middleware to authenticate the credentials passed while calling the API. Then I would add roles to the people so that only some of the roles would add the permission to add or delete data in the database.
2. I would add transaction management while deleting the entries or while doing any operation in the database so that the operations are not in a half done state.
3. Develop a UI and display the json objects so that they are easily understandable.
4. The `delete` API is doing a hard delete meaning it is deleting the entry from the table. We would not have any information about the deleted entries later. I would create an archive table and store the deleted entries there so that we would have a chance to look them if needed. Another approach would be to create a `status` field for each JSON object. It would be set to `active` when it is stored in the database. It would be sent to `inactive` when it is deleted. It would be a soft delete then.
5. I would add pagination to the `GET` API. Right now it is fetching all the entries and showing it to the user which is not very good. Pagination support should be added.
6. I would add environment specific application properties. Right now, the application properties are the same even if the application is run on local or on production. There has to be different set of properties (API endpoint, MongoDB URI) for local development and production development. 

## Author
- Supreetha Somasundar
