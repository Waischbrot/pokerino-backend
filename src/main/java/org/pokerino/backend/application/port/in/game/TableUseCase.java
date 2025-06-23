package org.pokerino.backend.application.port.in.game;

import org.pokerino.backend.adapter.in.dto.game.HostGameDto;
import org.pokerino.backend.adapter.in.response.game.ActionsResponse;
import org.pokerino.backend.adapter.in.response.game.GameResponse;

public interface TableUseCase {
    GameResponse host(HostGameDto hostGameDto);
    GameResponse join(String code);
    void leave();
    GameResponse current();
    ActionsResponse currentActions();
}
