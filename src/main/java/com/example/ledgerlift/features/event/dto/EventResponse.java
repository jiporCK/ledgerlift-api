package com.example.ledgerlift.features.event.dto;

import com.example.ledgerlift.features.organization.dto.OrganizationResponse;

import java.math.BigDecimal;

public record EventResponse(

        String uuid,

        String name,

        String description,

        Boolean isVisible,

        BigDecimal goalAmount,

        OrganizationResponse organization

) {
}
