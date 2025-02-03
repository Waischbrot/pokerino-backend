# Pokerino Backend

A Spring Boot project that provides a REST API and Websocket service for a Poker game.

[![Java](https://img.shields.io/badge/Java-21-red.svg?logo=java)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen.svg?logo=spring)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.11.1-green.svg?logo=gradle)](https://gradle.org/)

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
    this.deckRankingUseCase.evaluateHand(hand);
}
```
