package com.example.ledgerlift.features.event.dto;

import java.math.BigDecimal;

public record EventRequest(

        String name,

        String description,

        BigDecimal goalAmount

) {
}
