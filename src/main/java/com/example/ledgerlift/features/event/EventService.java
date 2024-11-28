package com.example.ledgerlift.features.event;

import com.example.ledgerlift.features.event.dto.EventRequest;
import com.example.ledgerlift.features.event.dto.EventResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface EventService {

    void createEvent(String organizationUuid, String categoryUuid, EventRequest eventRequest);

    EventResponse getEventByUuid(String uuid);

    List<EventResponse> getAllEvents();

    void deleteEventByUuid(String uuid);

    void updateEventByUuid(String uuid, EventRequest eventRequest);

    List<EventResponse> getEventsByCategory(String uuid);

    List<EventResponse> getEventsByOrganization(String organizationUuid);

    void hideEventByUuid(String uuid);

    void setEventImage(String uuid, String image);

    void uploadImages(String uuid, @Valid List<String> images);
}
