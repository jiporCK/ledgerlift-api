package com.example.ledgerlift.features.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;

public record EventRequest(

        @NotBlank(message = "Event name is required")
        String name,

        @Size(max = 200,message = "Description must not exceed 200 characters")
        String description,

        @NotBlank(message = "Location is required")
        @Size(max = 50)
        String location,

        Date startDate,

        Date endDate

) {
}
