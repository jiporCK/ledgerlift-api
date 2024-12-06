package com.example.ledgerlift.features.organization;

import com.example.ledgerlift.domain.Event;
import com.example.ledgerlift.domain.Organization;
import com.example.ledgerlift.domain.Role;
import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.ca.CAService;
import com.example.ledgerlift.features.ca.dto.CAEnrollmentRequest;
import com.example.ledgerlift.features.event.EventRepository;
import com.example.ledgerlift.features.mail.MailService;
import com.example.ledgerlift.features.media.dto.ImageRequest;
import com.example.ledgerlift.features.organization.dto.OrganizationRequest;
import com.example.ledgerlift.features.organization.dto.OrganizationResponse;
import com.example.ledgerlift.features.user.UserRepository;
import com.example.ledgerlift.init.RoleRepository;
import com.example.ledgerlift.mapper.OrganizationMapper;
import com.example.ledgerlift.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrganizationMapper organizationMapper;
    private final MailService mailService;
    private final EventRepository eventRepository;
    private final CAService caService;

    @Override
    public void createOrganization(String userUuid, OrganizationRequest request) {

        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User with uuid " + userUuid + " not found"
                        )
                );

        Organization organization = organizationMapper.fromOrganizationRequest(request);

        if (organizationRepository.existsByName(request.name())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Organization with name " + request.name() + " already exists"
            );
        }

        organization.setUuid(Utils.generateUuid());
        organization.setUser(user);
        organization.setIsLocked(false);
        organization.setIsApproved(false);

        organizationRepository.save(organization);
        mailService.welcomeOrganization(organization);

    }

    @Override
    public OrganizationResponse getOrganizationByUuid(String uuid) {
        return null;
    }

    @Override
    public List<OrganizationResponse> getAll() {

        List<Organization> organizations = organizationRepository.findAll();

        log.info("Organizations : {}", organizations.size());

        return organizationMapper.toOrganizationResponseList(organizations);
    }

    @Override
    public void deleteOrganizationByUuid(String uuid) {

        Organization organization = organizationRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Organization with uuid " + uuid + " not found"
                        )
                );

        organizationRepository.delete(organization);

    }

    @Override
    public void updateOrganizationByUuid(String uuid, OrganizationRequest request) {

        Organization organization = organizationMapper.fromOrganizationRequest(request);

        organizationRepository.save(organization);

    }

    @Override
    public void uploadQrImage(String organizationUuid, ImageRequest qrImage) {

        Organization organization = organizationRepository.findByUuid(organizationUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Organization with uuid " + organizationUuid + " not found"
                        )
                );

        organization.setMoneyQRCode(qrImage.image());

    }

    @Override
    public List<OrganizationResponse> getOrganizationsByUserUuid(String userUuid) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "User with uuid " + userUuid + " not found"
                        )
                );

        List<Organization> organizations = user.getOrganizations();
        if (organizations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with uuid " + userUuid + " does not have any organizations");
        }

        return organizationMapper.toOrganizationResponseList(organizations);

    }

    @Transactional
    @Override
    public void lockOrganization(String organizationUuid) {

        Organization organization = organizationRepository.findByUuid(organizationUuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Organization with uuid " + organizationUuid + " not found"
                ));

        organizationRepository.lockByUuid(organization.getUuid());

        List<Event> events = organization.getEvents();
        if (events != null && !events.isEmpty()) {
            events.forEach(event -> event.setIsVisible(Boolean.FALSE));

            eventRepository.saveAll(events);
        }
    }

    @Transactional
    @Override
    public void unlockOrganization(String organizationUuid) {

        Organization organization = organizationRepository.findByUuid(organizationUuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Organization with uuid " + organizationUuid + " not found"
                ));

        organizationRepository.unlockByUuid(organization.getUuid());

        List<Event> events = organization.getEvents();
        if (events != null && !events.isEmpty()) {
            events.forEach(event -> event.setIsVisible(Boolean.TRUE));

            eventRepository.saveAll(events);
        }
    }

    @Override
    public List<OrganizationResponse> getPendingOrganization() {

        List<Organization> organizations = organizationRepository.findAll()
                .stream().filter(
                        org -> org.getIsApproved().equals(Boolean.FALSE)
                ).toList();

        if (organizations.isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "There is no pending organization"
            );

        }

        return organizationMapper.toOrganizationResponseList(organizations);
    }

    @Transactional
    @Override
    public void approveOrg(String organizationUuid) {

        Organization organization = organizationRepository.findByUuid(organizationUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Organization with uuid " + organizationUuid + " not found"
                        )
                );

        if (organization.getIsApproved().equals(Boolean.TRUE)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Organization is already approved"
            );
        }

        organizationRepository.approveByUuid(organizationUuid);

        try {

            CAEnrollmentRequest caEnrollmentRequest = CAEnrollmentRequest.builder()
                    .username("organization" + organization.getUuid())
                    .affiliation("org1.department1")
                    .type("client")
                    .secret(Utils.generateUuid())
                    .orgName("Org1")
                    .genSecret(true)
                    .registrarUsername("admin")
                    .build();

            caService.registerAndEnrollUser(caEnrollmentRequest.getUsername(), caEnrollmentRequest);

            User user = organization.getUser();
            List<Role> roles = user.getRoles();
            Role organizer = roleRepository.findByName("ORGANIZER");
            roles.add(organizer);

            user.setRoles(roles);
            userRepository.save(user);

        } catch (ResponseStatusException e) {
            // If the exception is related to already registered or enrolled user, allow the login to proceed
            if (!e.getMessage().contains("already enrolled")) {
                throw e;  // Rethrow if it's a different error
            }
            log.warn("User {} already enrolled, proceeding with login.", organization.getUuid());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
