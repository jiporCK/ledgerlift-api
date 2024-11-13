package com.example.ledgerlift.features.auth.dto;

import com.example.ledgerlift.features.user.userdto.UserResponse;

public record AuthResponse(

        String type,

        String accessToken,

        String refreshToken,

        UserResponse user

) {
}