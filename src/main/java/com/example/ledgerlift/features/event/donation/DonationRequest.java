package com.example.ledgerlift.features.event.donation;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DonationRequest(

        BigDecimal amount,

        String message

) {
}
