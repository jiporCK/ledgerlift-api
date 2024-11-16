package com.example.ledgerlift.features.auth;

import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.auth.dto.AuthResponse;
import com.example.ledgerlift.features.auth.dto.LoginRequest;
import com.example.ledgerlift.features.auth.dto.RefreshTokenRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }

    @Override
    public AuthResponse refresh(RefreshTokenRequest request) {
        return null;
    }
}
