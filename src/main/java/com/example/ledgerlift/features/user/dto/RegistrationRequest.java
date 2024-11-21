package com.example.ledgerlift.features.user.dto;

public record RegistrationRequest(

        String phoneNumber,

        String email,

        String username,

        String password

) {
}
