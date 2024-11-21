package com.example.ledgerlift.features.organization.dto;

import com.example.ledgerlift.features.user.dto.UserResponse;

public record OrganizationResponse(

        String uuid,

        String name,

        String description,

        String email,

        String phone,

        String address,

        UserResponse user

) {
}
