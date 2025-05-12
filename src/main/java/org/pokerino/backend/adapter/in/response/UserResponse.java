package org.pokerino.backend.adapter.in.response;

import java.util.Date;

public record UserResponse(
        String username,
        Date joinDate,
        long chips,
        int gold,
        long experience
) {}
