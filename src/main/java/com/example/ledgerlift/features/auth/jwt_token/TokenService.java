package com.example.ledgerlift.features.auth.jwt_token;

import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.auth.dto.AuthResponse;
import org.springframework.security.core.Authentication;

public interface TokenService {

    AuthResponse createToken(User user, Authentication authentication);

    String createAccessToken(Authentication authentication);

    String createRefreshToken(Authentication authentication);

}
