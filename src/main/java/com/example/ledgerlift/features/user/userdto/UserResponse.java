package com.example.ledgerlift.features.user.userdto;

public record UserResponse(

        Long id,

        String uuid,

        String username,

        String password,

        String phoneNumber,

        String email,

        String avatar,

        Boolean isProfiledVisibility,

        Boolean isEmailVerified

) {
}
