
# Backend for an E-commerce API made with Spring Boot

## Description

This project's aim is to create an API capable of managing various products' orders made by registered users with different levels of authorization through the use of a REST architecture using CRUD operations.




## Requirements
This application will require the installation of `JAVA 24` and the creation of a `Stripe` account since the payments make use of their API.\
It is also required the creation of a database (by default it's `PostgreSQL`), costumizing the DB may require changes to the codebase.
## Environment Variables

To run this project, you will need to add the following environment variables to your `application.properties`:

- a 256 bit secret key
- the public key generated upon registering a `Stripe` account
- the secret key generated upon registering a `Stripe` account
- the name of the database 
- the username of the user managing the database
- the password of the user managing the database


## Deployment

Navigate to the project folder or use an IDE and deploy the project through the terminal with the command:

```bash
  .\mvnw clean install 
```
This will result in the creation of an executable `.jar` file in the target folder of the project.\
To run it open the terminal in and type the following command:
```bash
  java -jar ecommerceApi-0.0.1-SNAPSHOT.jar 
```
