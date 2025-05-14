package org.pokerino.backend.application.port.in;

import org.pokerino.backend.adapter.in.response.AddExperienceResponse;
import org.pokerino.backend.adapter.in.response.UserResponse;

public interface UserUseCase {
    UserResponse getMe();
    UserResponse getUser(String username);
    AddExperienceResponse addExperience(int experience);
}
