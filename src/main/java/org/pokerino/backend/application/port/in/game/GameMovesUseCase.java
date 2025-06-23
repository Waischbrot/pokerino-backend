package org.pokerino.backend.application.port.in.game;

public interface GameMovesUseCase {
    void fold(long playerId);
    void call(long playerId);
    void raise(long playerId, int numberOfChips);
    void allIn(long playerId);
    void check(long playerId);
}
 