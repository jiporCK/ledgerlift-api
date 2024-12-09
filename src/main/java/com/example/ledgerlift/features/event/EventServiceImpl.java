package com.example.ledgerlift.features.event;

import com.example.ledgerlift.domain.Category;
import com.example.ledgerlift.domain.Event;
import com.example.ledgerlift.domain.Organization;
import com.example.ledgerlift.features.catetory.CategoryRepository;
import com.example.ledgerlift.features.event.dto.EventRequest;
import com.example.ledgerlift.features.event.dto.EventResponse;
import com.example.ledgerlift.features.media.dto.ImageRequest;
import com.example.ledgerlift.features.organization.OrganizationRepository;
import com.example.ledgerlift.mapper.EventMapper;
import com.example.ledgerlift.utils.Utils;
import com.google.api.Http;
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

    private final EventRepository eventRepository;
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

        if (eventRepository.existsByNameEqualsIgnoreCase(request.name())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Event is already existed"
            );
        }

        event.setUuid(Utils.generateUuid());
        event.setIsUrgent(false);
        event.setOrganization(organization);
        event.setCategory(category);

        eventRepository.save(event);

    }

    @Override
    public EventResponse getEventByUuid(String uuid) {

        Event event = eventRepository.findByUuid(uuid)
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

        List<Event> events = eventRepository.findAll();

        if (events.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "There is nothing to show!"
            );
        }

        return eventMapper.toEventResponseList(events);
    }

    @Override
    public List<EventResponse> getUrgentEvents() {

        List<Event> events = eventRepository.findAll().stream()
                .filter(
                        event -> event.getIsUrgent().equals(Boolean.TRUE)
                ).toList();

        if (events.isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "There is no urgent event!"
            );

        }

        return eventMapper.toEventResponseList(events);
    }

    @Override
    public void deleteEventByUuid(String uuid) {

        Event event = eventRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Event has not been found!"
                        )
                );

        eventRepository.delete(event);

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
    public void setEventImage(String uuid, ImageRequest image) {

        Event event = eventRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Event has not been found!"
                        )
                );

        event.setImage(image.image());
        eventRepository.save(event);

    }

    @Transactional
    @Override
    public void setUrgentEvent(String uuid) {

        if (!eventRepository.existsByUuid(uuid)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Event has not been found"
            );

        }

        eventRepository.makeUrgentByUuid(uuid);

    }
}
