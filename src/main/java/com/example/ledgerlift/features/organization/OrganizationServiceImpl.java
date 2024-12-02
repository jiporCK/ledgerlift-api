package com.example.ledgerlift.features.organization;

import com.example.ledgerlift.domain.Organization;
import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.mail.MailService;
import com.example.ledgerlift.features.media.dto.ImageRequest;
import com.example.ledgerlift.features.organization.dto.OrganizationRequest;
import com.example.ledgerlift.features.organization.dto.OrganizationResponse;
import com.example.ledgerlift.features.user.UserRepository;
import com.example.ledgerlift.mapper.OrganizationMapper;
import com.example.ledgerlift.utils.Utils;
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
    private final OrganizationMapper organizationMapper;
    private final MailService mailService;

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
}
