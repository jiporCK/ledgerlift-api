package com.example.ledgerlift.features.user.forgetpassworddto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordResetRequest(

        @Email
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        String newPassword,

        @NotBlank(message = "Confirm password is required")
        String confirmPassword

) {

}
