package com.example.ledgerlift.features.receipt;

import com.example.ledgerlift.domain.Event;
import com.example.ledgerlift.domain.Receipt;
import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.receipt.dto.ReceiptResponse;

import java.util.List;

public interface ReceiptService {

    List<ReceiptResponse> getReceiptByUserUuid(String userUuid);

    void createReceipt(User user, Event event);

}
