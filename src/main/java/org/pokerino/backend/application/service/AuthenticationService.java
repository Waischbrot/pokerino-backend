package org.pokerino.backend.application.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.pokerino.backend.adapter.in.dto.LoginUserDto;
import org.pokerino.backend.adapter.in.dto.RegisterUserDto;
import org.pokerino.backend.application.port.in.AuthenticationUseCase;
import org.pokerino.backend.application.port.out.LoadUserPort;
import org.pokerino.backend.application.port.out.SaveUserPort;
import org.pokerino.backend.domain.user.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    LoadUserPort loadUserPort;
    SaveUserPort saveUserPort;

    // Todo: Check if there is already a User with this Mail (Or test -> the user with email x might not even be created, since db has unique field)
    // Todo: Filter against email domain blacklist for antibot protection
    // Todo: Check if there is already a User with this Username (Or test as described above)
    @Override
    public void signup(RegisterUserDto registerUserDto) {
        final String encodedPassword = this.passwordEncoder.encode(registerUserDto.password());
        final User user = new User(registerUserDto.username(), registerUserDto.email(), encodedPassword);
        this.saveUserPort.saveUser(user);
    }

    // Needs heavy rework!
    @Override
    public User authenticate(LoginUserDto loginUserDto) {
        final User user = this.loadUserPort.findByEmail(loginUserDto.email())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Todo: Catch FUCKING AuthenticationException because otherwise your apartment will burn down!
        // Todo: This is kind of bad -> requests user data again, login could be verified right here!
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), loginUserDto.password())); // -> Maybe UserDetails / User object can be extracted from this?
        return user;
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return false;
    }
}