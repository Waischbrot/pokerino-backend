package org.pokerino.backend.application.port.in;

import org.pokerino.backend.adapter.in.dto.LoginUserDto;
import org.pokerino.backend.adapter.in.dto.RegisterUserDto;
import org.pokerino.backend.domain.user.User;

public interface AuthenticationUseCase {
    void signup(RegisterUserDto registerUserDto);

    User authenticate(LoginUserDto loginUserDto);

    boolean isUsernameTaken(String username);
}
