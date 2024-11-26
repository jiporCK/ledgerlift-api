package com.example.ledgerlift.features.user.dto;

public record UserResponse(

        String uuid,

        String firstName,

        String lastName,

        String username,

        String password,

        String phoneNumber,

        String email,

        String avatar,

        Boolean isProfiledVisibility,

        Boolean isEmailVerified

) {
}
