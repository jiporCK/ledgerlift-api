package com.example.ledgerlift.features.user.dto;

import jakarta.validation.constraints.NotBlank;

import java.sql.Date;

public record UserUpdateRequest(

        @NotBlank(message = "Fist name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Gender is required")
        String gender,

        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Phone number is required")
        String phoneNumber,

        @NotBlank(message = "Date of birth is required")
        Date dateOfBirth

) {
}
