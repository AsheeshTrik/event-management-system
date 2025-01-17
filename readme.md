# Event Management System

## Overview
The **Event Management System** is a microservices-based application designed to handle users, events, and ticket bookings seamlessly. It uses Eureka for service discovery, allowing interaction between the microservices without hardcoding URLs. Below is an outline of the system's structure, functionality, and instructions for setup and testing.

---

## Project Structure
```
event-management-system
|
├── eureka-service        # Service Discovery using Netflix Eureka
|
├── user-service          # Handles user-related operations
|
├── event-service         # Manages events and related details
|
├── ticket-service        # Facilitates ticket bookings and interactions between user and event services
```

---

## Microservices

### 1. **Eureka Service**
The Eureka service is a service discovery server used to manage communication between the microservices.

- **Port:** `8761`
- **Dependencies:**
    - `spring-cloud-starter-netflix-eureka-server`
- **Setup:**
    - Add `@EnableEurekaServer` to the main application class.

---


### 2. **Event Service**
Handles event creation and management.

- **Port:** `8081`
- **Dependencies:**
    - `spring-cloud-starter-netflix-eureka-client`
    - `spring-boot-starter-data-jpa`
    - `spring-boot-starter-web`
    - `MySQL database`

- **Application Configuration:**
```yaml
server:
  port: 8081
spring:
  application:
    name: event-service
  datasource:
    url: jdbc:mysql://localhost:3306/eventdb
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

- **Key Classes:**
    - `Event`: Entity class for event details.
    - `EventRepository`: JPA repository for database interactions.
    - `EventService`: Business logic for event operations.
    - `EventController`: Exposes REST endpoints for event-related actions.

- **Endpoints:**
    - `POST /events`: Create a new event.
    - `GET /events/{id}`: Fetch event details by ID.

---

### 3. **User Service**
Manages user-related operations like creating users and fetching user details.

- **Port:** `8082`
- **Dependencies:**
    - `spring-cloud-starter-netflix-eureka-client`
    - `spring-boot-starter-data-jpa`
    - `spring-boot-starter-web`
    - `MySQL database`

- **Application Configuration:**
```yaml
server:
  port: 8082
spring:
  application:
    name: event-service
  datasource:
    url: jdbc:mysql://localhost:3306/userdb
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

- **Key Classes:**
    - `User`: Entity class for user details.
    - `UserRepository`: JPA repository for database interactions.
    - `UserService`: Business logic for user operations.
    - `UserController`: Exposes REST endpoints for user-related actions.

- **Endpoints:**
    - `POST /users`: Create a new user.
    - `GET /users/{id}`: Fetch user details by ID.
    - `GET /users/username/{username}`: Fetch user details by username.

---

### 4. **Ticket Service**
Facilitates ticket booking and integrates with the User and Event services using Eureka.

- **Port:** `8083`
- **Dependencies:**
    - `spring-cloud-starter-netflix-eureka-client`
    - `spring-boot-starter-data-jpa`
    - `spring-boot-starter-web`
    - `MySQL database`

- **Application Configuration:**
```yaml
server:
  port: 8083
spring:
  application:
    name: event-service
  datasource:
    url: jdbc:mysql://localhost:3306/ticketdb
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

- **Key Classes:**
    - `Ticket`: Entity class for ticket details.
    - `TicketRepository`: JPA repository for database interactions.
    - `TicketService`: Handles ticket booking logic and communicates with User and Event services.
    - `TicketController`: Exposes REST endpoints for ticket-related actions.

- **Endpoints:**
    - `POST /tickets`: Book a ticket.
    - `GET /tickets/{id}`: Fetch ticket details by ID.
    - `GET /tickets/user/{userId}`: Fetch all tickets for a user.

- **Integration Logic:**
    - The `TicketService` uses Spring WebClient to interact with the User and Event services via Eureka.
    - Example:
      ```java
      String userUrl = "http://user-service/users/" + userId;
      String eventUrl = "http://event-service/events/" + eventId;
      ```

---

## Testing with Postman

### 1. User Service
- **Create User:**
    - **Method:** `POST`
    - **URL:** `http://localhost:8082/users`
    - **Body:**
      ```json
      {
        "username": "john_doe",
        "email": "john.doe@example.com",
        "password": "password123"
      }
      ```

- **Fetch User by ID:**
    - **Method:** `GET`
    - **URL:** `http://localhost:8082/users/1`

- **Fetch User by Username:**
    - **Method:** `GET`
    - **URL:** `http://localhost:8082/users/username/john_doe`

### 2. Event Service
- **Create Event:**
    - **Method:** `POST`
    - **URL:** `http://localhost:8081/events`
    - **Body:**
      ```json
      {
        "name": "Tech Conference",
        "description": "A conference about the latest in tech."
      }
      ```

- **Fetch Event by ID:**
    - **Method:** `GET`
    - **URL:** `http://localhost:8081/events/1`

### 3. Ticket Service
- **Book Ticket:**
    - **Method:** `POST`
    - **URL:** `http://localhost:8081/tickets`
    - **Body:**
      ```json
      {
        "eventId": 1,
        "userId": 1
      }
      ```

- **Fetch Ticket by ID:**
    - **Method:** `GET`
    - **URL:** `http://localhost:8083/tickets/1`

- **Fetch Tickets by User ID:**
    - **Method:** `GET`
    - **URL:** `http://localhost:8083/tickets/user/1`

---

## Notes
- Start the Eureka service first to ensure service registration.
- Ensure each service is running on its respective port.
- Use Postman to test endpoints.
- The application uses a locally hosted MySQL database

