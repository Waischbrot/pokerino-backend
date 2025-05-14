package org.pokerino.backend.adapter.in.response;

public record ExperienceResponse(
        int level,
        long currentExperience,
        long requiredExperience
) {}
