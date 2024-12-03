package com.example.ledgerlift.features.receipt;

import com.example.ledgerlift.domain.Event;
import com.example.ledgerlift.domain.Organization;
import com.example.ledgerlift.domain.Receipt;
import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.event.EventRepository;
import com.example.ledgerlift.features.event.donation.DonationRequest;
import com.example.ledgerlift.features.mail.MailService;
import com.example.ledgerlift.features.receipt.dto.ReceiptResponse;
import com.example.ledgerlift.features.user.UserRepository;
import com.example.ledgerlift.mapper.ReceiptMapper;
import com.example.ledgerlift.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final UserRepository userRepository;
    private final ReceiptMapper receiptMapper;
    private final MailService mailService;
    private final EventRepository eventRepository;

    @Override
    public List<ReceiptResponse> getReceiptByUserUuid(String userUuid) {

        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "User has not been found"
                        )
                );

        List<Receipt> receipts = receiptRepository.findAllByUser(user);

        return receiptMapper.toReceiptResponseList(receipts);
    }

    @Override
    public ReceiptResponse createReceipt(User user, Event event, DonationRequest request) {

        Organization organization = event.getOrganization();

        String receiptId = Utils.generateUuid();
        String transactionId = Utils.generateUuid();

        Receipt receipt = new Receipt();
        receipt.setEvent(event);
        receipt.setOrganization(organization);
        receipt.setTransactionId(transactionId);
        receipt.setReceiptId(receiptId);
        receipt.setUser(user);
        receipt.setAmount(request.amount());
        receipt.setMessage(request.message());
        receipt.setDate(LocalDateTime.now());

        receiptRepository.save(receipt);

        mailService.sendDonationReceipt(user, receipt);

        BigDecimal raised = event.getCurrentRaised();
        event.setCurrentRaised(raised.add(request.amount()));

        eventRepository.save(event);

        return ReceiptResponse.builder()
                .receiptId(receipt.getReceiptId())
                .date(receipt.getDate())
                .transactionId(receipt.getTransactionId())
                .organizationName(organization.getName())
                .organizationContact(organization.getPhone())
                .donorEmail(user.getEmail())
                .donorName(user.getUsername())
                .campaignName(event.getName())
                .amount(request.amount())
                .message(request.message())
                .build();

    }

}
