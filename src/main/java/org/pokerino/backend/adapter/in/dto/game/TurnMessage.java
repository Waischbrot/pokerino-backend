package org.pokerino.backend.adapter.in.dto.game;

import org.pokerino.backend.domain.game.Action;

public record TurnMessage(
    String token,
    Action action,
    long chips
) {}