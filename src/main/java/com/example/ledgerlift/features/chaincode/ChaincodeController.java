package com.example.ledgerlift.features.chaincode;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chaincode")
public class ChaincodeController {

    private final ChaincodeService chaincodeService;

}
