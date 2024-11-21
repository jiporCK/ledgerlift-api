package com.example.ledgerlift.mapper;

import com.example.ledgerlift.domain.Event;
import com.example.ledgerlift.features.event.dto.EventRequest;
import com.example.ledgerlift.features.event.dto.EventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    Event fromEventCreateRequest(EventRequest request);

    @Mapping(source = "organization", target = "organization")
    EventResponse toEventResponse(Event event);

}
