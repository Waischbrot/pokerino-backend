# Pokerino Backend

A modern Spring Boot backend for an online Poker platform, built with Java 21.
This project uses Websockets for real-time gameplay and exposes REST endpoints everything else like auth.

[![Java](https://img.shields.io/badge/Java-21-red.svg?logo=java)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen.svg?logo=spring)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.11.1-green.svg?logo=gradle)](https://gradle.org/)

---

## Used Technologies

- Java 21 + Spring Boot
- Gradle
- MariaDB
- Docker
- Websockets + Rest
- JWT for authentication

---

## Configuration

The backend is configured via environment variables. The following variables are required:

| Variable Name         | Description                        | Example Value      | Required |
|-----------------------|------------------------------------|--------------------|----------|
| `SERVER_PORT`         | Port for the backend server        | `8080`             | Yes      |
| `MARIADB_HOST`        | MariaDB database host              | `localhost`        | Yes      |
| `MARIADB_PORT`        | MariaDB database port              | `3306`             | Yes      |
| `MARIADB_NAME`        | MariaDB database name              | `pokerino`         | Yes      |
| `MARIADB_USERNAME`    | MariaDB username                   | `pokeruser`        | Yes      |
| `MARIADB_PASSWORD`    | MariaDB password                   | `supersecret`      | Yes      |
| `JWT_SECRET_KEY`      | Secret key for JWT (HS256)         | `myjwtsecretkey`   | Yes      |
| `JWT_EXPIRATION_TIME` | JWT expiration time in ms          | `3600000`          | Yes      |

**Note:**
- All variables are required for the application to start successfully.
- `JWT_SECRET_KEY` should be generated cryptographically secure.
- `JWT_EXPIRATION_TIME` is in milliseconds (e.g., `3600000` for 1 hour).

---

## Rest Authentication

### **POST** `/auth/signup`
Register a new account to the backend
> Body
> ```json
> {
>   "username": "username",
>   "email": "email",  
>   "password": "password"
> }
> ```

> Return:
> - OK
> - Internal Server Error

### **POST** `/auth/login`
Login to an existing account using email
> Body
> ```json
> {
>   "email": "email",
>   "password": "password"
> }
> ```

> Return:
> ```json
> {
>   "token": "token",
>   "user": {
>     "username": "username",
>     "joinDate": "date",
>     "chips": 0,
>     "experience": {
>       "level": 0,
>       "currentExperience": 0,
>       "requiredExperience": 0
>     }
>   }
> }
> ```

### GET 

---

## Rest User Management

---

## Rest Table/Session Management

---

## Rest Game Information

---

## Websocket Connect to App

---

## Websocket Outbound Notifications

---

## Websocket Inbound Messages

---

## Final Words

This project was a group effort for a course in our studies called "Web Engineering".
Although we chose a challenging topic and lost one teammate along the way due to exmatriculation,
we truly enjoyed working on it together.

Thank you for checking out our project! ❤️