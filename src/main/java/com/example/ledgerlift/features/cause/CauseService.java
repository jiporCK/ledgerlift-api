package com.example.ledgerlift.features.cause;

import com.example.ledgerlift.features.cause.dto.CauseRequest;
import com.example.ledgerlift.features.cause.dto.CauseResponse;

import java.util.List;

public interface CauseService {

    void createCause(String organizationUuid, String categoryUuid, CauseRequest causeRequest);

    CauseResponse getCauseByUuid(String uuid);

    List<CauseResponse> getAllCauses();

    void deleteCauseByUuid(String uuid);

    void updateCauseByUuid(String uuid, CauseRequest causeRequest);

}
