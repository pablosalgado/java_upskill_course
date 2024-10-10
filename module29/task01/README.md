# Database Configuration


The default configured database is PostgreSQL. Please create the database named `module29_task01` and update the credentials in the `application.properties`

# Run the Application

Execute the following commands to start the application in `async` mode. We are using the ActiveMQ broker instaled locally:

```bash
cd task01
export SPRING_PROFILES_ACTIVE=dev,async
mvn clean package
brew services start activemq
java -jar target/task01-1.0.0.jar
```

Execute the following commands to start the application in `sync` mode:

```bash
cd task01
export SPRING_PROFILES_ACTIVE=dev,sync
mvn clean package
brew services stop activemq
java -jar target/task01-1.0.0.jar
```

The application will be available at:

[http://localhost:8080](http://localhost:8080)

After successfully start, you can seed the database with the following command:

```bash
curl --location --request POST 'localhost:8080/tickets/seed'
```

**Note:** You can also run the application in sync mode by changing from `spring.profiles.active=async` to `spring.profiles.active=sync` and compiling again.


# API Documentation

The API documentation is available at:

https://documenter.getpostman.com/view/24753894/2sAXxLCZsU