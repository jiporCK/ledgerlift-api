package com.example.ledgerlift.mapper;

import com.example.ledgerlift.domain.Receipt;
import com.example.ledgerlift.features.receipt.dto.ReceiptResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    @Mapping(source = "user.username", target = "donorName")
    @Mapping(source = "user.email", target = "donorEmail")
    @Mapping(source = "organization.name", target = "organizationName")
    @Mapping(source = "organization.phone", target = "organizationContact")
    @Mapping(source = "receiptId", target = "receiptId")
    ReceiptResponse toReceiptResponse(Receipt receipt);

    @Mapping(source = "donorName", target = "user.username")
    @Mapping(source = "donorEmail", target = "user.email")
    @Mapping(source = "organizationName", target = "organization.name")
    @Mapping(source = "organizationContact", target = "organization.phone")
    @Mapping(source = "receiptId", target = "receiptId")
    Receipt toReceipt(ReceiptResponse receiptResponse);

    List<ReceiptResponse> toReceiptResponseList(List<Receipt> receipts);
}

