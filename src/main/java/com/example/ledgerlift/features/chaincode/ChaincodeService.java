package com.example.ledgerlift.features.chaincode;

import org.hyperledger.fabric.gateway.ContractException;

import java.util.concurrent.TimeoutException;

public interface ChaincodeService {


    void invoke(String chaincodeName, String methodName, String ...args) throws ContractException, InterruptedException, TimeoutException;

    String query(String chaincodeName, String functionName, String ...args) throws ContractException;

}
