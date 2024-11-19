package com.example.ledgerlift.mapper;

import com.example.ledgerlift.domain.Cause;
import com.example.ledgerlift.features.cause.dto.CauseRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CauseMapper {

    Cause fromCauseCreateRequest(CauseRequest request);

}
