This is a skeleton of Spring Boot application which should be used as a start point to create a working one.
The goal of this task is to create simple REST API  which allows users to manage TODOs.
The API should allow to create/delete/update TODOs and categories as well as search for user, name, description, deadline and category in any combination. *For example find all todos for an user X where deadline is today and name contains test.*
The API should also implement basic authorization/authentication: *User X cannot access TODOs of user Y as long as he doesn't have admin role.*

You are free to use any library or testing framework in the project.

Below you may find a proposition of the DB model:

![DB model](DBModel.png)


## How to run

Start postgres container:
```bash
cd docker
docker compose up -d
```

Start the application with 'dev' profile, to initialize the database with some test data:
```bash
cd ..
./gradlew bootRun --args='--spring.profiles.active=dev'
```


## Further enhancements

- Generate the api models and controller endpoints using openapi-generator
- Use gradle dependency catalog to organize dependencies and their versions
- Improve error responses
- Add more tests
- Add checkstyle/pmd/spotbugs
- Add structured logging support

## Testing

Run automated tests with:
```bash
cd ..
./gradlew test
```

You can find a hoppscotch collection at `hoppscotch/hoppscotch-collection.json` to test the API manually.
