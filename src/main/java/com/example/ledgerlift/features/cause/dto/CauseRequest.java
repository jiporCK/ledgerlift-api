package com.example.ledgerlift.features.cause.dto;

import java.math.BigDecimal;

public record CauseRequest(

        String name,

        String description,

        BigDecimal goalAmount

) {
}
