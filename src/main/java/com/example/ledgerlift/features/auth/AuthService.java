package com.example.ledgerlift.features.auth;

import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.auth.dto.AuthResponse;
import com.example.ledgerlift.features.auth.dto.LoginRequest;
import com.example.ledgerlift.features.auth.dto.RefreshTokenRequest;
import org.springframework.security.core.Authentication;

public interface AuthService {


    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshTokenRequest request);
}
