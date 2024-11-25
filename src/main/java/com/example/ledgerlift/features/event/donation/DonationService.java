package com.example.ledgerlift.features.event.donation;

import com.example.ledgerlift.domain.Event;
import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.event.EventRepository;
import com.example.ledgerlift.features.receipt.ReceiptRepository;
import com.example.ledgerlift.features.receipt.ReceiptService;
import com.example.ledgerlift.features.receipt.dto.ReceiptResponse;
import com.example.ledgerlift.features.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpService;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ReceiptService receiptService;

    public ReceiptResponse donate(String userUuid, String eventUuid, DonationRequest donationRequest) {

        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "User not found"
                        )
                );

        Event event = eventRepository.findByUuid(eventUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Event not found"
                        )
                );

        receiptService.createReceipt(user, event, donationRequest);

        return ReceiptResponse.builder()
                .build();

    }

}
