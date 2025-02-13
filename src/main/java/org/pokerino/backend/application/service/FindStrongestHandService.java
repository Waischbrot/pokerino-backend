package org.pokerino.backend.application.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.application.port.in.DeckRankingUseCase;
import org.pokerino.backend.application.port.in.FindStrongestHandUseCase;
import org.pokerino.backend.domain.game.GamePlayer;
import org.pokerino.backend.domain.game.PokerGame;
import org.pokerino.backend.domain.rank.DeckRanking;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FindStrongestHandService implements FindStrongestHandUseCase {
    DeckRankingUseCase deckRankingUseCase;

    @Override
    public List<GamePlayer> findStrongestHands(final PokerGame game) {
        final List<GamePlayer> winners = new ArrayList<>();
        DeckRanking bestRanking = null;
        for (final GamePlayer player : game.getPlayers()) {
            final DeckRanking ranking = this.deckRankingUseCase.evaluateHand(player.getHand());
            final int evaluation = ranking.playAgainst(bestRanking);
            if (bestRanking == null || evaluation > 0) { // Is the new best ranking
                bestRanking = ranking;
                winners.clear();
                winners.add(player);
            } else if (evaluation == 0) { // Is equal to the prior best ranking, very rare case
                winners.add(player);
            }
        }
        return winners;
    }
}