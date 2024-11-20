package com.example.ledgerlift.features.chaincode;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.gateway.ContractException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chaincode")
public class ChaincodeController {

    private final ChaincodeService chaincodeService;

    @PostMapping("/{chaincodeName}/{functionName}/invoke")
    public String invokeChaincode(
            @PathVariable String chaincodeName,
            @PathVariable String functionName

    ) throws Exception {
        chaincodeService.invoke(chaincodeName,
                functionName, "null");
        return "Invoke chaincode " + functionName + " successfully!!!";
    }

    @GetMapping("/{chaincodeName}/{functionName}/query")
    public ResponseEntity<String> queryChaincode(
            @PathVariable String chaincodeName,
            @PathVariable String functionName,
            @RequestParam(required = false) String assetId) {

        try {
            String result = chaincodeService.query(chaincodeName, functionName, assetId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error executing query: " + e.getMessage());
        }
    }

}
