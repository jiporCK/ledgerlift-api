package com.example.ledgerlift.features.receipt;

import com.example.ledgerlift.domain.Receipt;
import com.example.ledgerlift.features.receipt.dto.ReceiptResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    @GetMapping("/{userUuid}")
    public List<ReceiptResponse> getReceiptByUserUuid(@PathVariable("userUuid") String userUuid) {

        return receiptService.getReceiptByUserUuid(userUuid);

    }

}
