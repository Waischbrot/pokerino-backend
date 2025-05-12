package org.pokerino.backend.application.port.in;

import io.jsonwebtoken.Claims;
import org.pokerino.backend.domain.user.User;

import java.util.Map;
import java.util.function.Function;

public interface JWTUseCase {
    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(User user);

    String generateToken(Map<String, Object> extraClaims, User user);

    long getExpirationTime();

    boolean isTokenValid(String token, String username);

    boolean isTokenValid(String token);
}
