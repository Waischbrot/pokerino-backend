package org.pokerino.backend.application.port.out;

import org.pokerino.backend.domain.user.User;

public interface SaveUserPort {
    User saveUser(User user);
}
