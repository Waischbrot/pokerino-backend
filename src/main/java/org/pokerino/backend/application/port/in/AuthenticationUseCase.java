package org.pokerino.backend.application.port.in;

import org.pokerino.backend.adapter.in.dto.LoginUserDto;
import org.pokerino.backend.adapter.in.dto.RegisterUserDto;
import org.pokerino.backend.domain.user.User;

public interface AuthenticationUseCase {
    void signup(RegisterUserDto registerUserDto);

    User authenticate(LoginUserDto loginUserDto);

    void verifyUser(String verificationCode);

    void resendVerificationCode(String email);

    boolean isUsernameTaken(String username);
}
