package com.example.ledgerlift.features.chaincode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeoutException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChaincodeServiceImpl implements ChaincodeService {

    private final Gateway gateway;

    @Override
    public void invoke(String chaincodeName, String functionName, String... args) throws ContractException, InterruptedException, TimeoutException {

        Network network = gateway.getNetwork("mychnannel");
        Contract contract = network.getContract(chaincodeName);

        var result = contract.submitTransaction(functionName, args);
        
        log.info("Result: {}", result);

    }

    @Override
    public String query(String chaincodeName, String functionName, String... args) throws ContractException {
        
        Network network = gateway.getNetwork("mychnannel");
        Contract contract = network.getContract(chaincodeName);
        
        byte[] result;
        if (args.length > 0 && args[0] != null && !args[0].isEmpty()) {
            result = contract.evaluateTransaction(functionName, args);
        } else {
            result = contract.evaluateTransaction(functionName);
        }
        
        return new String(result);
    }

}
