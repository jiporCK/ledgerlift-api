package com.example.ledgerlift.features.user.dto;

import java.util.Date;

public record UserResponse(

        String uuid,

        String firstName,

        String lastName,

        String gender,

        String username,

        String password,

        String phoneNumber,

        String email,

        String avatar,

        Date dateOfBirth,

        Boolean isProfiledVisibility,

        Boolean isEmailVerified

) {
}
