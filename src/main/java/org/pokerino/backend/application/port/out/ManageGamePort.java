package org.pokerino.backend.application.port.out;

import org.pokerino.backend.domain.game.PokerGame;

public interface ManageGamePort {
    void saveGame(PokerGame pokerGame);

    void removeGame(String gameCode);

    String generateGameCode();
}
