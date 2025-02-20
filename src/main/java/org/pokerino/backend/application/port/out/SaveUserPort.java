package org.pokerino.backend.application.port.out;

import org.pokerino.backend.domain.user.User;

public interface SaveUserPort {
    void saveUser(User user);
}
