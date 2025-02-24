# Pokerino Backend

A Spring Boot project that provides a REST API and Websocket service for a Poker game.

[![Java](https://img.shields.io/badge/Java-21-red.svg?logo=java)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen.svg?logo=spring)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.11.1-green.svg?logo=gradle)](https://gradle.org/)
[![JUnit](https://img.shields.io/badge/JUnit-5.11.4-blue.svg?logo=junit)](https://junit.org/junit5/)

## External API Endpoints

Due to the different purposes which this backend serves, 2 different communication protocols are used: REST and Websockets.
e.g. REST for user authentication or character information and Websockets for the gameplay itself.

### Authentication (REST)

#### **POST** `/auth/signup`
Register a new account to the backend
> Input (data to create a new account)
> ```json
> {
>   "username": "username",
>   "email": "email",  
>   "password": "password"
> }
> ```

> Return (new account data if successful)
> ```json
> {
>   "id": 1,
>   "username": "username",
>   "email": "email",
>   "enabled": false
> }
> ```

### **POST** `/auth/login`
Login to an existing account using email
> Input (data to login)
> ```json
> {
>   "email": "email",
>   "password": "password"
> }
> ```

> Return (JWT token if successful)
> ```json
> {
>   "token": "token",
>   "expiresIn": "<milliseconds>"
> }
> ```

## Internal API (use case)

Internal services and adapters can interact with each other using UseCases.
A UseCase is an interface that contains methods specific to the service implementation.

Because of the way Spring works, these internal dependencies are injected automatically if only one constructor exists
or if the constructor is annotated with `@Autowired`. [Spring Docs](https://docs.spring.io/spring-framework/reference/core/beans/annotation-config/autowired.html)

### Ranking the value of a given hand

```Java
private final DeckRankingUseCase deckRankingUseCase;

public void test() {
    String[] hand = { "2H", "3D", "5S", "9C", "KD", "2C", "3H" };
    DeckRanking ranking = deckRankingUseCase.evaluateHand(hand);
    // Do stuff with ranking
}
```

[Interface](src/main/java/org/pokerino/backend/application/port/in/DeckRankingUseCase.java) | 
[Service Implementation](src/main/java/org/pokerino/backend/application/service/DeckRankingService.java)

### Finding the players with the strongest hands

```Java
private final FindStrongestHandUseCase findStrongestHandUseCase;

public void test(PokerGame game) {
    // Call to find the players with the strongest hands this round
    List<GamePlayer> winningPlayers = findStrongestHandUseCase.findStrongestHands(game);
}
```

[Interface](src/main/java/org/pokerino/backend/application/port/in/FindStrongestHandUseCase.java) |
[Service Implementation](src/main/java/org/pokerino/backend/application/service/FindStrongestHandService.java)

## Domain Model

### Card Stacks

Imagine a CardStack instance just like a stack of cards placed on the table, where you can take cards from.

> Creating a new instance:
> ```Java
> CardStack deck = CardStack.create();
>```

> Taking a card from the stack:
> ```Java
> String card = deck.take();
> ```
> Throws an IllegalStateException if the stack is empty.

[Implementation](src/main/java/org/pokerino/backend/domain/cards/CardStack.java)

### Game Instances

For every running game, there has to be one game instance in the backend.
This instance does not implement the game logic, but it holds the necessary data to do so.

> Creating a new instance:
> ```Java
> // Initialise table information here and parse to constructor
> PokerGame game = new PokerGame(table);
> ```

PokerGame implements the [Joinable](src/main/java/org/pokerino/backend/domain/game/Joinable.java) interface, which adds basic user management.

> How to use:
> ```Java
> // Add a user to the game
> game.addPlayer(long userId);
> // Remove a user from the game
> game.removePlayer(long userId);
> // Check if a user is in the game
> game.contains(long userId);
> // Get the number of players in the game
> game.currentPlayers();
> // Get max amount of players
> game.maxPlayers();
> ```

[Implementation](src/main/java/org/pokerino/backend/domain/game/PokerGame.java)

Every user within a game is stored as [GamePlayer](src/main/java/org/pokerino/backend/domain/game/GamePlayer.java), which holds the following attributes for the game:

- `int total` - The total amount of chips
- `int bet` - The current bet
- `String[] hand` - The user's hand
