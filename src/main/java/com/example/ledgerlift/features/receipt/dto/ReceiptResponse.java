package com.example.ledgerlift.features.receipt.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ReceiptResponse(

        String receiptId,

        String donorName,

        String donorEmail,

        String campaignName,

        BigDecimal amount,

        String message,

        LocalDateTime date,

        String transactionId,

        String organizationName,

        String organizationContact

) {
}
