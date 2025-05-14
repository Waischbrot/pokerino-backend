package org.pokerino.backend.application.port.in;

import org.pokerino.backend.adapter.in.response.AddExperienceResponse;
import org.pokerino.backend.adapter.in.response.ExperienceResponse;
import org.pokerino.backend.domain.user.User;

public interface LevelUseCase {
   AddExperienceResponse addExperience(User user, long exp);
   ExperienceResponse calculateLevel(User user);

}
