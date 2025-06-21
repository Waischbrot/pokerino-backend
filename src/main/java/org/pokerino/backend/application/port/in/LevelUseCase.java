package org.pokerino.backend.application.port.in;

import org.pokerino.backend.adapter.in.response.ExperienceResponse;
import org.pokerino.backend.domain.user.User;

import java.util.List;

public interface LevelUseCase {
   List<Integer> addExperience(User user, long exp);
   ExperienceResponse calculateLevel(User user);

}
