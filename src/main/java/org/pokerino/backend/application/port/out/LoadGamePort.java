package org.pokerino.backend.application.port.out;

import org.pokerino.backend.domain.game.PokerGame;

import java.util.Optional;

public interface LoadGamePort {
    Optional<PokerGame> getGame(String gameCode);

    Optional<PokerGame> getUserGame(String username);
}
