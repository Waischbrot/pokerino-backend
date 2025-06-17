package org.pokerino.backend.adapter.in.response;

import java.util.List;

/**
 * This class has been used for a self-service endpoint during the development phase.
 * Not destined for production use.
 */
@Deprecated
public record AddExperienceResponse(
        ExperienceResponse experience,
        List<Integer> levelUps
) {}
