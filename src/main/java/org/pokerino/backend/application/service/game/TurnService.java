package org.pokerino.backend.application.service.game;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.application.port.in.game.TurnUseCase;
import org.pokerino.backend.domain.game.GamePlayer;
import org.pokerino.backend.domain.game.PokerGame;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class TurnService implements TurnUseCase {
    @Override
    public void fold(PokerGame game, GamePlayer player) {

    }

    @Override
    public void check(PokerGame game, GamePlayer player) {

    }

    @Override
    public void call(PokerGame game, GamePlayer player) {

    }

    @Override
    public void raise(PokerGame game, GamePlayer player, int numberOfChips) {

    }

    @Override
    public void allIn(PokerGame game, GamePlayer player) {

    }
}
