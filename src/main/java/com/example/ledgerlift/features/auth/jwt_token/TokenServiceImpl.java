package com.example.ledgerlift.features.auth.jwt_token;

import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.auth.dto.AuthResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService{

    @Override
    public AuthResponse createToken(User user, Authentication authentication) {
        return null;
    }

    @Override
    public String createAccessToken(Authentication authentication) {
        return "";
    }

    @Override
    public String createRefreshToken(Authentication authentication) {
        return "";
    }

}
