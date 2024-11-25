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
import jakarta.transaction.Transactional;
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
    private final EventRepository eventRepository;

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
    public void updateEventByUuid(String uuid, EventRequest request) {

        Event event = eventRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Event has not been found!"
                        )
                );

        event.setName(request.name());
        event.setDescription(request.description());
        event.setGoalAmount(request.goalAmount());

        eventRepository.save(event);
    }

    @Override
    public List<EventResponse> getEventsByCategory(String uuid) {

        Category category = categoryRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Category has not been found!"
                        )
                );

        List<Event> events = eventRepository.findAllByCategory(category);

        return eventMapper.toEventResponseList(events);
    }

    @Override
    public List<EventResponse> getEventsByOrganization(String organizationUuid) {

        Organization organization = organizationRepository.findByUuid(organizationUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Organization has not been found!"
                        )
                );

        List<Event> events = eventRepository.findAllByOrganization(organization);

        return eventMapper.toEventResponseList(events);
    }

    @Override
    public void hideEventByUuid(String uuid) {

        Event event = eventRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Event has not been found!"
                        )
                );

        event.setIsVisible(true);

        eventRepository.save(event);

    }

    @Override
    public void setEventImage(String uuid, String image) {

        Event event = eventRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Event has not been found!"
                        )
                );

        event.setImage(image);
        eventRepository.save(event);

    }
}
