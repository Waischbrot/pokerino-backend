package org.pokerino.backend.domain.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.dto.HostGameDto;
import org.pokerino.backend.domain.outbound.exception.BadRequestException;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class TableOptions {
    String name;
    int maxPlayers;
    int turnTime;
    long startBalance;
    long smallBlind;
    boolean increasingBlind;

    private TableOptions(HostGameDto options){
        this.name = options.name();
        this.maxPlayers = options.maxPlayers();
        this.turnTime = options.turnTime();
        this.startBalance = options.startBalance();
        this.smallBlind = options.smallBlind();
        this.increasingBlind = options.increasingBlind();
    }

    @NonNull
    public static TableOptions fromRequest(HostGameDto options) {
        if (options.maxPlayers() < 2 || options.maxPlayers() > 6) {
            throw new BadRequestException("Max players must be between 2 and 6.");
        }
        if (options.turnTime() < 30 || options.turnTime() > 120) {
            throw new BadRequestException("Turn time must be between 30 and 120 seconds.");
        }
        if (options.startBalance() < 100 || options.startBalance() > 10000) {
            throw new BadRequestException("Starting balance must be between 100 and 10,000 chips.");
        }
        if (options.smallBlind() < 5 || options.smallBlind() > (options.startBalance() / 10)) {
            throw new BadRequestException("Small blind must be between 5 and 10% of the starting balance.");
        }
        return new TableOptions(options);
    }
}