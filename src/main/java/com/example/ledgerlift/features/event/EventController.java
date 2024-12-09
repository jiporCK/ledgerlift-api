package com.example.ledgerlift.features.event;

import com.example.ledgerlift.base.BasedMessage;
import com.example.ledgerlift.features.event.dto.EventRequest;
import com.example.ledgerlift.features.event.dto.EventResponse;
import com.example.ledgerlift.features.media.dto.ImageRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{organizationUuid}/{categoryUuid}")
    public void createCause(@PathVariable String organizationUuid,
                            @PathVariable String categoryUuid,
                            @Valid @RequestBody EventRequest request) {

        eventService.createEvent(organizationUuid, categoryUuid, request);

    }

    @GetMapping
    public List<EventResponse> getAll() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{categoryUuid}")
    public List<EventResponse> getEventsByCategory(@PathVariable String categoryUuid) {

        return eventService.getEventsByCategory(categoryUuid);

    }

    @GetMapping("/urgent-events")
    public List<EventResponse> getUrgentEvents() {

        return eventService.getUrgentEvents();

    }


    @GetMapping("/{organizationUuid}")
    public List<EventResponse> getEventsByOrganization(@PathVariable String organizationUuid) {

        return eventService.getEventsByOrganization(organizationUuid);

    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{uuid}/hide-event")
    public void hideByUuid(@PathVariable String uuid) {

        eventService.hideEventByUuid(uuid);

    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{uuid}")
    public void updateEventByUuid(@PathVariable String uuid,
                                  @Valid @RequestBody EventRequest request) {

        eventService.updateEventByUuid(uuid, request);

    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{uuid}/upload-image")
    public void updateEventImage(@PathVariable String uuid,
                                 @Valid @RequestBody ImageRequest image) {

        eventService.setEventImage(uuid, image);

    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{uuid}/urgent")
    public void updateUrgentEvent(@PathVariable String uuid) {

        eventService.setUrgentEvent(uuid);

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{eventUuid}")
    public BasedMessage deleteEvent(@PathVariable String eventUuid) {

        eventService.deleteEventByUuid(eventUuid);

        return BasedMessage.builder()
                .message("Event deleted successfully")
                .build();

    }

}
