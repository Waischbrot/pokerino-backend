package org.pokerino.backend.application.port.out;

import org.pokerino.backend.domain.user.User;

import java.util.Optional;

public interface LoadUserPort {
    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationCode(String verificationCode);
}
