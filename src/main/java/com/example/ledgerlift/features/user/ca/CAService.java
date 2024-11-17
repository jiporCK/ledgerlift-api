package com.example.ledgerlift.features.user.ca;

import com.example.ledgerlift.features.user.ca.dto.CAEnrollmentRequest;

public interface CAService {

    void registerAndEnrollUser(CAEnrollmentRequest request) throws Exception;

}
