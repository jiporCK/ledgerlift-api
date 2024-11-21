package com.example.ledgerlift.features.event;

import com.example.ledgerlift.features.event.dto.EventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/causes")
public class EventController {

    private final EventService eventService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{organizationUuid}/{categoryUuid}")
    public void createCause(@PathVariable String organizationUuid,
                            @PathVariable String categoryUuid,
                            @RequestBody EventRequest request) {

        eventService.createEvent(organizationUuid, categoryUuid, request);

    }

}
