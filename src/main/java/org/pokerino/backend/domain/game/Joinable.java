package org.pokerino.backend.domain.game;

public interface Joinable {
    void addPlayer(long userId);
    void removePlayer(long userId);
    boolean contains(long userId);
    int currentPlayers();
    int maxPlayers();
}
