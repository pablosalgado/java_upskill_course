# Task 1 - SpringJDBC-based Social Network
## Run the application

Default configured database is H2:

```
cd task01
mvn clean install
mvn spring-boot:run
```

# Task 2 - Highload Writing Console Tool

## Database configuration

Default configured database is PostgreSQL with the `dev` profile:

```
cd task02
src/main/resources/application-dev.properties
```

## Application configuration

The application configuration is done via a JSON file:

```
cd task02
config.json
```

Schema as follows:

```
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "object",
  "properties": {
    "concurrency": {
      "type": "integer",
      "minimum": 1
    },
    "tableConfigList": {
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "columnCount": {
            "type": "integer",
            "minimum": 1
          },
          "typeCount": {
            "type": "integer",
            "minimum": 1
          },
          "rowCount": {
            "type": "integer",
            "minimum": 1
          }
        },
        "required": ["columnCount", "typeCount", "rowCount"]
      }
    }
  },
  "required": ["concurrency", "tableConfigList"]
}
```

`concurrency`: Specifies the number of threads to utilize, denoted as (L). This setting controls the parallelism level for operations that can be executed concurrently.

`tableConfigList`: Represents a user-defined array of size (N), where each element in the array corresponds to the configuration for one of (N) tables to be created. Each configuration item details:

- The number of columns (K) to be created in the table.
- The number of distinct data types (Z) to be used in these columns, with a current maximum of 14 distinct types.
- The total number of rows (m) to populate in the table.

This structure allows users to define multiple table configurations, each specifying the schema and size of the table, which can be processed using the specified level of concurrency.


## Run the application
```
cd task02
mvn clean install
mvn spring-boot:run
```
