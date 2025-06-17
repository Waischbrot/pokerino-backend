package org.pokerino.backend.application.port.out;

import org.pokerino.backend.domain.game.PokerGame;

import java.util.List;
import java.util.Optional;

public interface LoadGamePort {
    Optional<PokerGame> getGame(String gameCode);

    Optional<PokerGame> getGame(long userId);

    List<PokerGame> getPublicQueues();
}
