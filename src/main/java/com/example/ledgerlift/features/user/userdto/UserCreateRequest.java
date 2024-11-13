package com.example.ledgerlift.features.user.userdto;

public record UserCreateRequest(

        String phoneNumber,

        String email,

        String username,

        String password

) {
}
