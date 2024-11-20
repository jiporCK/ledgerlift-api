package com.example.ledgerlift.mapper;

import com.example.ledgerlift.domain.Cause;
import com.example.ledgerlift.features.cause.dto.CauseRequest;
import com.example.ledgerlift.features.cause.dto.CauseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CauseMapper {

    Cause fromCauseCreateRequest(CauseRequest request);

    @Mapping(source = "organization", target = "organization")
    CauseResponse toCauseResponse(Cause cause);

}
