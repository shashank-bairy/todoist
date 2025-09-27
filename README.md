# Todoist

A Dropwizard-based REST API for managing todos with CRUD operations, built using Guice dependency injection and following MVC pattern. The project is organized as a Maven multi-module project with clear separation of concerns.

## Features

- **CRUD Operations**: Create, Read, Update, Delete todos
- **Dependency Injection**: Uses Google Guice for dependency management
- **MVC Pattern**: Clean separation of concerns with Model, View (REST), and Controller layers
- **Lombok Integration**: Reduces boilerplate code with annotations
- **Validation**: Input validation using Jakarta Validation annotations
- **Exception Handling**: Custom exception mappers for proper error responses
- **Health Checks**: Built-in health monitoring
- **In-Memory Storage**: Thread-safe in-memory data store with repository pattern
- **Logging**: Comprehensive logging with SLF4J and Logback

## Project Structure

The project is organized as a Maven multi-module project:

```
todoist/
├── pom.xml                    # Parent POM
├── todoist-models/           # Models module
│   ├── pom.xml
│   └── src/main/java/com/bairy/todoist/
│       └── core/
│           ├── Todo.java
│           └── dto/
│               ├── CreateTodoRequest.java
│               └── UpdateTodoRequest.java
└── todoist-server/           # Server module
    ├── pom.xml
    └── src/main/java/com/bairy/todoist/
        ├── TodoistApplication.java
        ├── TodoistConfiguration.java
        ├── TodoistModule.java
        ├── api/
        │   └── TodoResource.java
        ├── core/
        │   ├── TodoService.java
        │   └── exception/
        │       ├── TodoNotFoundException.java
        │       ├── TodoNotFoundExceptionMapper.java
        │       └── ValidationExceptionMapper.java
        ├── db/
        │   ├── TodoRepository.java
        │   └── InMemoryTodoRepository.java
        └── health/
            └── TodoHealthCheck.java
```

## Architecture

The application follows a clean architecture with the following layers:

- **Models Module**: Contains `Todo` entity and DTOs (pure data models)
- **Server Module**: Contains the Dropwizard application, services, controllers, repository implementations, and exception handling
- **Dependency Injection**: Google Guice for managing dependencies
- **MVC Pattern**: Clean separation of concerns with Model, View (REST), and Controller layers
- **Lombok Integration**: Reduces boilerplate code with `@Data`, `@Builder`, `@Slf4j`, etc.

## Multi-Module Benefits

- **Separation of Concerns**: Models are pure data structures, server contains all implementation details
- **Reusability**: Models can be shared across different applications (web, mobile, etc.)
- **Independent Development**: Teams can work on models and server independently
- **Clean Dependencies**: Clear dependency hierarchy (server depends on models)
- **Better Architecture**: Repository layer belongs to server implementation, not models
- **Easier Testing**: Each module can be tested independently
- **Scalability**: Easy to add new modules (e.g., todoist-client, todoist-web)

## Lombok Benefits

- **Reduced Boilerplate**: Automatic generation of getters, setters, equals, hashCode, toString
- **Builder Pattern**: `@Builder` annotation for fluent object creation
- **Logging**: `@Slf4j` annotation for automatic logger injection
- **Constructor Injection**: `@RequiredArgsConstructor` for dependency injection
- **Exception Handling**: `@StandardException` for clean exception definitions
- **Data Classes**: `@Data` annotation for complete data classes
- **Cleaner Code**: More readable and maintainable codebase

## API Endpoints

### Create Todo
```
POST /todos
Content-Type: application/json

{
  "title": "Sample Todo",
  "description": "This is a sample todo item"
}
```

### Get All Todos
```
GET /todos
```

### Get Todos by Completion Status
```
GET /todos/filter?completed=true
GET /todos/filter?completed=false
```

### Get Todo by ID
```
GET /todos/{id}
```

### Update Todo
```
PUT /todos/{id}
Content-Type: application/json

{
  "title": "Updated Todo",
  "description": "Updated description",
  "completed": true
}
```

### Delete Todo
```
DELETE /todos/{id}
```

### Get Todo Count
```
GET /todos/count
```

### Health Check
```
GET /healthcheck
```

## Running the Application

1. **Build the application**:
   ```bash
   mvn clean package -DskipTests
   ```

2. **Run the application**:
   ```bash
   java -jar todoist-server/target/todoist-server-1.0-SNAPSHOT.jar server todoist-server/src/main/resources/config.yml
   ```

3. **Access the application**:
   - API: http://localhost:8080
   - Admin: http://localhost:8081
   - Health Check: http://localhost:8081/healthcheck

## Configuration

The application uses `config.yml` for configuration:

```yaml
server:
  applicationConnectors:
    - type: http
      port: 8080
  adminConnectors:
    - type: http
      port: 8081

logging:
  level: INFO
  loggers:
    com.bairy.todoist: DEBUG
  appenders:
    - type: console
```

## Dependencies

- **Dropwizard 5.0.0**: Web framework
- **Google Guice 7.0.0**: Dependency injection
- **Jakarta Validation**: Input validation
- **Jackson**: JSON serialization/deserialization

## Testing the API

### Using curl

1. **Create a todo**:
   ```bash
   curl -X POST http://localhost:8080/todos \
     -H "Content-Type: application/json" \
     -d '{"title": "Learn Dropwizard", "description": "Study Dropwizard framework"}'
   ```

2. **Get all todos**:
   ```bash
   curl http://localhost:8080/todos
   ```

3. **Update a todo**:
   ```bash
   curl -X PUT http://localhost:8080/todos/1 \
     -H "Content-Type: application/json" \
     -d '{"title": "Learn Dropwizard", "description": "Study Dropwizard framework", "completed": true}'
   ```

4. **Delete a todo**:
   ```bash
   curl -X DELETE http://localhost:8080/todos/1
   ```

## Error Handling

The API returns appropriate HTTP status codes:

- `200 OK`: Successful GET/PUT operations
- `201 Created`: Successful POST operations
- `204 No Content`: Successful DELETE operations
- `400 Bad Request`: Validation errors
- `404 Not Found`: Todo not found
- `500 Internal Server Error`: Server errors

Error responses include a JSON object with error details:

```json
{
  "code": 400,
  "message": "Validation failed: Title cannot be blank"
}
```
