package org.pokerino.backend.application.port.in.game;

import org.pokerino.backend.adapter.in.response.game.ActionsResponse;
import org.pokerino.backend.adapter.in.response.game.GameResponse;
import org.pokerino.backend.domain.game.PokerGame;

public interface GetGameUseCase {
    GameResponse toResponse(PokerGame pokerGame, String username);
    ActionsResponse availableActions(PokerGame pokerGame, String username);
}
