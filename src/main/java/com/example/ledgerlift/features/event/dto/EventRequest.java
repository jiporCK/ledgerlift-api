package com.example.ledgerlift.features.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record EventRequest(

        @NotBlank(message = "Event name is required")
        String name,

        @Size(max = 200,message = "Description must not exceed 200 characters")
        String description,

        @NotBlank(message = "Location is required")
        @Size(max = 50)
        String location

) {
}
