package org.pokerino.backend.domain.game;

public interface Joinable {
    void addPlayer(long userId);
    void removePlayer(long userId);
    void removePlayerFromQueue(long userId);
    boolean containsInGame(long userId);
    boolean containsInQueue(long userId);
    int currentPlayers();
    int maxPlayers();
}
