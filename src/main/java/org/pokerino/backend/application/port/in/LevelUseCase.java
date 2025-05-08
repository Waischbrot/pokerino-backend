package org.pokerino.backend.application.port.in;

import org.pokerino.backend.domain.user.User;

public interface LevelUseCase {

   int calculateLevel(long exp);
   void addExperience(User user, long exp);
}
