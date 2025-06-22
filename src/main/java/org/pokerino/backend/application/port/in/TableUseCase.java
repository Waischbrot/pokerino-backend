package org.pokerino.backend.application.port.in;

import org.pokerino.backend.adapter.in.dto.HostGameDto;
import org.pokerino.backend.adapter.in.response.ActionsResponse;
import org.pokerino.backend.adapter.in.response.GameResponse;

public interface TableUseCase {
    GameResponse host(HostGameDto hostGameDto);
    GameResponse join(String code);
    void leave();
    GameResponse current();
    ActionsResponse currentActions();
}
