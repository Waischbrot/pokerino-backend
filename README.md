# Pokerino Backend

A Spring Boot project that provides a REST API and Websocket service for a Poker game.

[![Java](https://img.shields.io/badge/Java-21-red.svg?logo=java)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen.svg?logo=spring)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.11.1-green.svg?logo=gradle)](https://gradle.org/)
[![JUnit](https://img.shields.io/badge/JUnit-5.11.4-blue.svg?logo=junit)](https://junit.org/junit5/)

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
