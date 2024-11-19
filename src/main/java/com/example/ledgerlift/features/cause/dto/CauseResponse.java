package com.example.ledgerlift.features.cause.dto;

import com.example.ledgerlift.features.organization.dto.OrganizationResponse;

import java.math.BigDecimal;

public record CauseResponse(

        String uuid,

        String name,

        String description,

        BigDecimal goalAmount,

        OrganizationResponse organization

) {
}
