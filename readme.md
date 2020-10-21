# A simple Spring Boot REST auth with JWT.

Source of last #hacKoding session:  
Teach participants how to create an Authentication REST API that generates JWT tokens with Spring Boot.

## Tech 
* [Spring Boot](https://www.baeldung.com/spring-boot)
* [JWT](https://jwt.io/)

##Feature

### Register
Create a new user:

* Url : `/api/auth/signup`
* Method : `POST`
* Data: `{
	"username":"user",
	"email":"user@demo.com",
	"password":"password",
	"role":["article"]
}`

### Login
 Authentication to get json web tokens:

* Url : `/api/auth/login`
* Method : `POST`
* Data: `{
	"username":"user",
	"password":"password",
}`

You have to create a file `'application.properties'` in `scr/main/resources` folder.

We used postgresql as the database. You can also use mysql or whatever. Adapt the configuration file according to your preference. For postgresql users the file should look like this :

```
spring.datasource.url= jdbc:postgresql://localhost:5432/database
spring.datasource.username= user
spring.datasource.password= password

spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation= true
spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.PostgreSQLDialect

# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto= update

# App Properties
authjwt.app.jwtSecret= authSecretKey
authjwt.app.jwtExpirationMs= 86400000
```


Enjoy!