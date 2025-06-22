package org.pokerino.backend.application.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.dto.HostGameDto;
import org.pokerino.backend.adapter.in.response.game.ActionsResponse;
import org.pokerino.backend.adapter.in.response.game.GameResponse;
import org.pokerino.backend.application.port.in.TableUseCase;
import org.pokerino.backend.application.port.out.LoadGamePort;
import org.pokerino.backend.application.port.out.ManageGamePort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class TableService implements TableUseCase {
    ManageGamePort manageGamePort;
    LoadGamePort loadGamePort;

    @Override
    public GameResponse host(HostGameDto hostGameDto) {
        return null;
    }

    @Override
    public GameResponse join(String code) {
        return null;
    }

    @Override
    public void leave() {

    }

    @Override
    public GameResponse current() {
        return null;
    }

    @Override
    public ActionsResponse currentActions() {
        return null;
    }

    @NonNull
    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
