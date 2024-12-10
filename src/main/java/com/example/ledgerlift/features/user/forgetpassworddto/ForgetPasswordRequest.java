package com.example.ledgerlift.features.user.forgetpassworddto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgetPasswordRequest(

        @Email
        @NotBlank(message = "Email is required")
        String email

) {
}
