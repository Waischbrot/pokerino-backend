# Pokerino Backend

A modern Spring Boot backend for an online Poker platform, built with Java 21.
This project uses Websockets for real-time gameplay and exposes REST endpoints everything else like auth.

[![Java](https://img.shields.io/badge/Java-21-red.svg?logo=java)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen.svg?logo=spring)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.11.1-green.svg?logo=gradle)](https://gradle.org/)

## Content
- [Used Technologies](#used-technologies)
- [Configuration](#configuration)
- [Rest Authentication](#rest-authentication)
    - [POST /auth/signup](#post-authsignup)
    - [POST /auth/login](#post-authlogin)
    - [GET /auth/username](#get-authusername)
    - [GET /auth/token](#get-authtoken)
- [Rest User Management](#rest-user-management)
    - [GET /user/me](#get-userme)
    - [GET /user](#get-user)
    - [POST /user/change-username](#post-userchange-username)
    - [POST /user/change-password](#post-userchange-password)
- [Rest Table/Session Management](#rest-tablesession-management)
    - [POST /table/host](#post-tablehost)
    - [POST /table/join](#post-tablejoin)
    - [POST /table/leave](#post-tableleave)
    - [GET /table/current](#get-tablecurrent)
    - [GET /table/current/actions](#get-tablecurrentactions)
    - [GET /table/current/cards](#get-tablecurrentcards)
- [Websocket Connect to App](#websocket-connect-to-app)
- [Websocket Outbound Notifications](#websocket-outbound-notifications)
- [Websocket Inbound Messages](#websocket-inbound-messages)
- [Final Words](#final-words)

## Used Technologies
- Java 21 + Spring Boot
- Gradle
- MariaDB
- Docker
- Websockets + Rest
- JWT for authentication

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

## Rest Authentication

### **POST** `/auth/signup`
Register a new account to the backend
> **Body:**
> ```json
> {
>   "username": "username",
>   "email": "email",  
>   "password": "password"
> }
> ```

> **Return:** `true`

---

### **POST** `/auth/login`
Login to an existing account using email
> **Body:**
> ```json
> {
>   "email": "email",
>   "password": "password"
> }
> ```

> **Return:**
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

---

### **GET** `/auth/username`
Get if this username is already taken
> **Query Parameter:** `username`

> **Return:** `true`

---

### **GET** `/auth/token`
Check if the provided JWT token is valid
> **Query Parameter:** `token`

> **Return:** `true`

---

## Rest User Management

### **GET** `/user/me`
Get the currently authenticated user's profile.

> **Headers:**  
> `Authorization: Bearer <token>`

> **Return:**
> ```json
> {
>   "username": "username",
>   "joinDate": "date",
>   "chips": 0,
>   "experience": {
>     "level": 0,
>     "currentExperience": 0,
>     "requiredExperience": 0
>   }
> }
> ```

---

### **GET** `/user`
Get a user's profile by username.

> **Headers:**  
> `Authorization: Bearer <token>`

> **Query Parameter:** `username`

> **Return:**
> ```json
> {
>   "username": "username",
>   "joinDate": "date",
>   "chips": 0,
>   "experience": {
>     "level": 0,
>     "currentExperience": 0,
>     "requiredExperience": 0
>   }
> }
> ```

---

### **POST** `/user/change-username`
Change the currently authenticated user's username.  
Returns a new JWT token for the updated username.

> **Headers:**  
> `Authorization: Bearer <token>`

> **Query Parameter:** `newUsername`

> **Return:** `new-jwt-token`

---

### **POST** `/user/change-password`
Change the currently authenticated user's password.

> **Headers:**  
> `Authorization: Bearer <token>`

> **Query Parameter:** `newPassword`

> **Return:** `true`

---

## Rest Table/Session Management

### **POST** `/table/host`
Host a new table/session.

> **Headers:**  
> `Authorization: Bearer <token>`

> **Body:**
> ```json
> {
>   "name": "My Poker Table",
>   "maxPlayers": 6,
>   "turnTime": 30,
>   "startBalance": 10000,
>   "smallBlind": 50,
>   "increasingBlind": true
> }
> ```

> **Return:**
> ```json
> {
>   "gameCode": "123456",
>   "gameState": "WAITING_FOR_PLAYERS",
>   "tableOptions": {
>     "name": "High Rollers Table",
>     "maxPlayers": 6,
>     "turnTime": 30,
>     "startBalance": 10000,
>     "smallBlind": 50,
>     "increasingBlind": true
>   },
>   "player": {
>     "username": "alice",
>     "host": false,
>     "cards": null,
>     "chips": 10000,
>     "bet": 0,
>     "folded": false,
>     "dead": false
>   },
>   "opponents": [],
>   "cardsOnTable": [null, null, null, null, null],
>   "currentPlayer": null,
>   "actions": null
> }
> ```

---

### **POST** `/table/join`
Join an existing table/session by code.

> **Headers:**  
> `Authorization: Bearer <token>`

> **Query Parameter:** `code`

> **Return:**
> ```json
> {
>   "gameCode": "123456",
>   "gameState": "WAITING_FOR_PLAYERS",
>   "tableOptions": {
>     "name": "High Rollers Table",
>     "maxPlayers": 6,
>     "turnTime": 30,
>     "startBalance": 10000,
>     "smallBlind": 50,
>     "increasingBlind": true
>   },
>   "player": {
>     "username": "alice",
>     "host": false,
>     "cards": null,
>     "chips": 10000,
>     "bet": 0,
>     "folded": false,
>     "dead": false
>   },
>   "opponents": [
>     {
>       "username": "bob",
>       "host": true,
>       "chips": 10000,
>       "bet": 0,
>       "folded": false,
>       "dead": false
>     }
>   ],
>   "cardsOnTable": [null, null, null, null, null],
>   "currentPlayer": null,
>   "actions": null
> }
> ```

---

### **POST** `/table/leave`
Leave the current table/session.

> **Headers:**  
> `Authorization: Bearer <token>`

---

### **GET** `/table/current`
Get the current table/session the user is in.

> **Headers:**  
> `Authorization: Bearer <token>`

> **Return:**
> ```json
> {
>   "gameCode": "482639",
>   "gameState": "IN_ROUND",
>   "tableOptions": {
>     "name": "High Rollers Table",
>     "maxPlayers": 6,
>     "turnTime": 30,
>     "startBalance": 10000,
>     "smallBlind": 50,
>     "increasingBlind": true
>   },
>   "player": {
>     "username": "alice",
>     "host": false,
>     "cards": ["As", "Kh"],
>     "chips": 8500,
>     "bet": 200,
>     "folded": false,
>     "dead": false
>   },
>   "opponents": [
>     {
>       "username": "bob",
>       "host": true,
>       "chips": 9000,
>       "bet": 200,
>       "folded": false,
>       "dead": false
>     },
>     {
>       "username": "carol",
>       "host": false,
>       "chips": 7800,
>       "bet": 0,
>       "folded": true,
>       "dead": false
>     }
>   ],
>   "cardsOnTable": ["2d", "5h", "9s", null, null],
>   "currentPlayer": "alice",
>   "actions": {
>     "actions": ["FOLD", "CALL", "RAISE"]
>   }
> }
> ```

---

### **GET** `/table/current/actions`
Get the list of available actions for the current player (if it's their turn).

> **Headers:**  
> `Authorization: Bearer <token>`

> **Return:**
> ```json
> {
>   "actions": ["FOLD", "CALL", "RAISE"]
> }
> ```

---

### **GET** `/table/current/cards`
Get your own current hand cards.

> **Headers:**
> `Authorization: Bearer <token>`

> **Return:**
> ```json
> {
>  "cards": ["As", "Kh"]
> }
> ```

---

## Websocket Connect to App

### **WebSocket Endpoint:** `/connect`

To connect to the application's WebSocket, use the following endpoint:

- **URL:**  
  `ws://<base-url>/connect`  
  or (if using SockJS fallback):  
  `http://<base-url>/connect`

SockJS is also supported!

---

**Example using JavaScript and [SockJS](https://github.com/sockjs/sockjs-client) with [STOMP.js](https://github.com/stomp-js/stompjs):**

```js
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const socket = new SockJS('http://<base-url>/connect');
const stompClient = new Client({
  webSocketFactory: () => socket,
  debug: (str) => console.log(str),
});

stompClient.onConnect = (frame) => {
  console.log('Connected: ' + frame);
  // Subscribe or send messages here
};

stompClient.activate();
```

---

## Websocket Outbound Notifications

During a game, the backend sends real-time notifications to all players in a table via WebSocket.  
**All notifications are sent to the destination:**
```
/topic/game/{gameCode}
```
where `{gameCode}` is the 6-digit code of the current game (e.g., `/topic/game/127413`).

---

### Outbound Message Types

All outbound messages include a `type` field indicating the event type.  
The following message types and payloads exist:

---

#### **LOG_ACTION**
A user made a turn (for logging purposes).

```json
{
  "type": "LOG_ACTION",
  "username": "alice",
  "action": "RAISE",
  "value": 500,
  "currentBet": 500,
  "allIn": false
}
```

---

#### **LOG_PLAYER_JOIN**
A new player joined the lobby.

```json
{
  "type": "LOG_PLAYER_JOIN",
  "username": "bob",
  "playerCount": 4
}
```

---

#### **LOG_PLAYER_LEAVE**
A player left the lobby.

```json
{
  "type": "LOG_PLAYER_LEAVE",
  "username": "carol",
  "playerCount": 3
}
```

---

#### **START_GAME**
The game started and is no longer joinable.

```json
{
  "type": "START_GAME",
  "playerCount": 4
}
```

---

#### **FINISH_GAME**
The game ended. This is the winner!

```json
{
  "type": "FINISH_GAME",
  "winner": "alice",
  "total": 40000
}
```

---

#### **NEXT_ROUND**
A new round is beginning.

> ⚠️ **Attention:** At this point, new cards have been issued to the players. These should be fetched over the REST API.

```json
{
  "type": "NEXT_ROUND",
  "smallBlindUsername": "bob",
  "smallBlind": 100,
  "bigBlindUsername": "carol",
  "bigBlind": 200
}
```

---

#### **END_ROUND**
The round ended. This is the winner of this round / showdown.

```json
{
  "type": "END_ROUND",
  "winners": ["alice"],
  "pot": 1200,
  "showdownCards": {
    "alice": ["As", "Kh"],
    "bob": ["9c", "9d"]
  }
}
```

---

#### **NEW_CARDS**
New cards opened on the table.

```json
{
  "type": "NEW_CARDS",
  "cardsOnTable": ["As", "Kh", "Qh, ","2d"],
  "newCards": ["2d"]
}
```

---

#### **TURN**
Current turn of the game.

```json
{
  "type": "TURN",
  "username": "alice",
  "actions": ["FOLD", "CALL", "RAISE"],
  "currentBet": 200,
  "now": 5,
  "ending": 30
}
```

---

## Websocket Inbound Messages
To perform a turn action in a poker game, send a message to the following STOMP destination:

> **Destination:**  
> `/app/game/turn`

>**Message Body:**
> ```json
> {
>   "token": "your-jwt-token",
>   "action": "FOLD",
>   "chips": 0
> }
> ```
> - `action` must be one of: `"FOLD"`, `"CHECK"`, `"CALL"`, `"RAISE"`, `"ALL_IN"`.  
    >   Only send actions that are currently available to you.
> - `chips` is **only used** when the action is `"RAISE"`; otherwise, it should be `0`.

---

## Final Words
This project was a group effort for a course in our studies called "Web Engineering".
Although we chose a challenging topic and lost one teammate along the way due to exmatriculation,
we truly enjoyed working on it together, writing thousands of lines in the progress.

Thank you for checking out our project! ❤️
