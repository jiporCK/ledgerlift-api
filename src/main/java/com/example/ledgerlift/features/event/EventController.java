package com.example.ledgerlift.features.event;

import com.example.ledgerlift.features.event.dto.EventRequest;
import com.example.ledgerlift.features.event.dto.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
                            @RequestBody EventRequest request) {

        eventService.createEvent(organizationUuid, categoryUuid, request);

    }

    @GetMapping("/{categoryUuid}")
    public List<EventResponse> getEventsByCategory(@PathVariable String categoryUuid) {

        return eventService.getEventsByCategory(categoryUuid);

    }

    @GetMapping("/{organizationUuid}")
    public List<EventResponse> getEventsByOrganization(@PathVariable String organizationUuid) {

        return eventService.getEventsByOrganization(organizationUuid);

    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{uuid}")
    public void hideByUuid(@PathVariable String uuid) {

        eventService.hideEventByUuid(uuid);

    }

}
