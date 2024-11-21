package com.example.ledgerlift.features.event;

import com.example.ledgerlift.domain.Category;
import com.example.ledgerlift.domain.Event;
import com.example.ledgerlift.domain.Organization;
import com.example.ledgerlift.features.catetory.CategoryRepository;
import com.example.ledgerlift.features.event.dto.EventRequest;
import com.example.ledgerlift.features.event.dto.EventResponse;
import com.example.ledgerlift.features.organization.OrganizationRepository;
import com.example.ledgerlift.mapper.EventMapper;
import com.example.ledgerlift.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository EventRepository;
    private final OrganizationRepository organizationRepository;
    private final CategoryRepository categoryRepository;

    private final EventMapper eventMapper;

    @Override
    public void createEvent(String organizationUuid, String categoryUuid, EventRequest request) {

        Organization organization = organizationRepository.findByUuid(organizationUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Organization has not been found!"
                        )
                );

        Category category = categoryRepository.findByUuid(categoryUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.CREATED,
                                "Category has not been found!"
                        )
                );

        Event event = eventMapper.fromEventCreateRequest(request);
        event.setUuid(Utils.generateUuid());
        event.setStartDate(LocalDateTime.now());
        event.setEndDate(LocalDateTime.now().plusMonths(1));
        event.setIsCompleted(false);
        event.setOrganization(organization);
        event.setCategory(category);

        EventRepository.save(event);

    }

    @Override
    public EventResponse getEventByUuid(String uuid) {

        Event event = EventRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Event has not been found!"
                        )
                );

        return eventMapper.toEventResponse(event);
    }

    @Override
    public List<EventResponse> getAllEvents() {
        return List.of();
    }

    @Override
    public void deleteEventByUuid(String uuid) {

    }

    @Override
    public void updateEventByUuid(String uuid, EventRequest EventRequest) {

    }
}
