package org.pokerino.backend.adapter.in.response;

import org.pokerino.backend.domain.game.Action;

import java.util.List;

public record ActionsResponse(List<Action> actions) {}
