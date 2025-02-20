package org.pokerino.backend.application.port.in;

import org.pokerino.backend.adapter.in.dto.LoginUserDto;
import org.pokerino.backend.adapter.in.dto.RegisterUserDto;
import org.pokerino.backend.adapter.in.dto.VerifyUserDto;
import org.pokerino.backend.domain.user.User;

public interface AuthenticationUseCase {
    User signup(RegisterUserDto regiserUserDto);

    User authenticate(LoginUserDto loginUserDto);

    void verifyUser(VerifyUserDto verifyUserDto);

    void resendVerificationCode(String email);
}
