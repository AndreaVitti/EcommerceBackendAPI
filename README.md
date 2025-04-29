
# Backend for an E-commerce API made with Spring Boot


## Description
This project's aim is to create an API capable of managing various products' orders made by registered users with different levels of authorization through the use of a REST architecture providing various CRUD operations.


## Functionalities
This API support the following operations:
- registration, login and logout of users
- store users' addresses 
- creation of different categories of products
- managing the different produts (adding, restocking, updating etc...)
- managing the different order placed by users 
- payment through the Stripe API

## Requirements
This application will require: 
- `JAVA 24` 
- a `Stripe` account since the payments make use of their API
- the creation of a `PostgreSQL` database (choosing a different DB may require changes to the codebase)


## Environment Variables
To run this project, you will need to add the following environment variables to your `application.properties`:
- a 256 bit secret key
- the public key generated upon registering a `Stripe` account
- the secret key generated upon registering a `Stripe` account
- the name of the database 
- the username of the user managing the database
- the password of the user managing the database


## Deployment
Clone the project:

```bash
  git clone https://github.com/AndreaVitti/EcommerceBackendAPI.git
```
Navigate to the project directory or use an IDE and deploy the project through the terminal with the command:

```bash
  .\mvnw clean install 
```
This will result in the creation of an executable `.jar` file in the target folder of the project.\
To run it open the terminal and type the following command:
```bash
  java -jar ecommerceApi-0.0.1-SNAPSHOT.jar 
```

## Testing
The endpoints of the API have been tested with the help of `Postman`.

## Usage
To use the project yourself I highly suggest to install `Postman` and proceed to check the various possible CRUD operations.\
It's could also be benefitial to create a frontend accordingly.
