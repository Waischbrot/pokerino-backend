package org.pokerino.backend.application.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.response.game.*;
import org.pokerino.backend.application.port.in.GetGameUseCase;
import org.pokerino.backend.domain.game.Action;
import org.pokerino.backend.domain.game.GamePlayer;
import org.pokerino.backend.domain.game.GameState;
import org.pokerino.backend.domain.game.PokerGame;
import org.pokerino.backend.domain.outbound.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class GetGameService implements GetGameUseCase {
    @Override
    public GameResponse toResponse(PokerGame pokerGame, String username) {
        final String gameCode = pokerGame.getGameCode();
        final GameState gameState = pokerGame.getState();
        final TableOptionsResponse tableOptions = new TableOptionsResponse(
                pokerGame.getOptions().getName(),
                pokerGame.getOptions().getMaxPlayers(),
                pokerGame.getOptions().getTurnTime(),
                pokerGame.getOptions().getStartBalance(),
                pokerGame.getOptions().getSmallBlind(),
                pokerGame.getOptions().isIncreasingBlind()
        );
        final var tempPlayer = pokerGame.getPlayer(username);
        final PlayerResponse player = new PlayerResponse(
                tempPlayer.getUsername(),
                tempPlayer.isHost(),
                tempPlayer.getHand() != null ? List.of(tempPlayer.getHand()) : null,
                tempPlayer.getChips(),
                tempPlayer.getBet(),
                tempPlayer.isFolded(),
                tempPlayer.isDead()
        );
        final List<OpponentResponse> opponents = pokerGame.getParticipants().stream()
                .filter(participant -> !participant.getUsername().equals(username))
                .map(participant -> new OpponentResponse(
                        participant.getUsername(),
                        participant.isHost(),
                        participant.getChips(),
                        participant.getBet(),
                        participant.isFolded(),
                        participant.isDead()
                ))
                .toList();
        final List<String> cardsOnTable = List.of(pokerGame.getCardsOnTable());
        String currentPlayer = null;
        ActionsResponse actions = null;
        if (gameState == GameState.IN_ROUND) {
            var tempCurrentPlayer = pokerGame.getCurrent();
            currentPlayer = tempCurrentPlayer.getUsername();
            if (currentPlayer.equals(username)) {
                var result = calculateAvailable(pokerGame, tempCurrentPlayer);
                actions = new ActionsResponse(result);
            }
        }
        return new GameResponse(
                gameCode,
                gameState,
                tableOptions,
                player,
                opponents,
                cardsOnTable,
                currentPlayer,
                actions
        );
    }

    @Override
    public ActionsResponse availableActions(PokerGame pokerGame, String username) {
        if (pokerGame.getState() != GameState.IN_ROUND) {
            throw new BadRequestException("Game: '" + pokerGame.getGameCode() + "' is not in a round!");
        }
        final var current = pokerGame.getCurrent();
        if (!current.getUsername().equals(username)) {
            throw new BadRequestException("It's not your turn in game: '" + pokerGame.getGameCode() + "'.");
        }
        final var result = calculateAvailable(pokerGame, current);
        return new ActionsResponse(result);
    }

    @NonNull
    private List<Action> calculateAvailable(PokerGame pokerGame, GamePlayer player) {
        if (player.isDead() || player.isFolded() || player.isAllIn()) {
            return List.of(); // No actions available if player is dead, folded, or all in
        }

        List<Action> available = new ArrayList<>();
        long currentBet = pokerGame.getCurrentBet();
        long toCall = currentBet - player.getBet();
        long minRaise = pokerGame.getMinRaise();

        // FOLD is always available if there is a bet to call
        if (toCall > 0) { // Don't check chips here as player would be all in
            available.add(Action.FOLD);
        }

        // CHECK is available if player doesn't need to call (i.e., already matched the current bet)
        if (toCall == 0) {
            available.add(Action.CHECK);
        }

        // CALL is available if player can fully match the bet without going all in
        if (toCall > 0 && player.getChips() > toCall) {
            available.add(Action.CALL);
        }

        // RAISE is available if player can call and then raise at least minRaise more
        if (toCall == 0 && player.getChips() >= minRaise) {
            // If no call is needed, but player can raise at least minRaise
            available.add(Action.RAISE);
        } else if (toCall > 0 && player.getChips() > toCall + minRaise) {
            // If call is needed, and player can call and raise at least minRaise more
            available.add(Action.RAISE);
        }

        // ALL_IN is always available if player has chips (and is not already all in)
        available.add(Action.ALL_IN);

        return available;
    }
}
