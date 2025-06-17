package org.pokerino.backend.adapter.in.response;

import java.util.Date;

public record UserResponse(
        String username,
        Date joinDate,
        long chips,
        ExperienceResponse experience
) {}
