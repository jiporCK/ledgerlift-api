package com.example.ledgerlift.features.event.donation;

import com.example.ledgerlift.base.BasedMessage;
import com.example.ledgerlift.features.receipt.dto.ReceiptResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/donate")
public class DonationController {

    private final DonationService donationService;

    @PostMapping("/{userUuid}/{eventUuid}")
    ReceiptResponse donate(@PathVariable String userUuid,
                        @PathVariable String eventUuid,
                        @Valid @RequestBody DonationRequest donationRequest) {

        return donationService.donate(userUuid, eventUuid, donationRequest);

    }

}
