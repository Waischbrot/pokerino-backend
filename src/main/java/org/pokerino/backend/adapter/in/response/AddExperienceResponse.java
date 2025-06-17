package org.pokerino.backend.adapter.in.response;

import java.util.List;

@Deprecated
public record AddExperienceResponse(
        ExperienceResponse experience,
        List<Integer> levelUps
) {}
