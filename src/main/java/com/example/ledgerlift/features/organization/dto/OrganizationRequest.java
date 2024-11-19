package com.example.ledgerlift.features.organization.dto;

public record OrganizationRequest(

        String name,

        String description,

        String email,

        String phone,

        String address

) {
}
