package org.pokerino.backend.adapter.in.response.game;

import org.pokerino.backend.domain.game.GameState;

import java.util.List;

public record GameResponse(
        String gameCode,
        GameState gameState,
        TableOptionsResponse tableOptions,
        PlayerResponse player, // The player who is currently viewing the game
        List<OpponentResponse> opponents, // Can be empty if no opponents are present yet or if game is over
        String currentPlayer, // Username of the player whose turn it is else null
        ActionsResponse actions // Can be null if not the player's turn
) {}
