package com.example.ledgerlift.features.organization;

import com.example.ledgerlift.base.BasedMessage;
import com.example.ledgerlift.domain.Organization;
import com.example.ledgerlift.features.mail.MailService;
import com.example.ledgerlift.features.media.dto.ImageRequest;
import com.example.ledgerlift.features.organization.dto.OrganizationRequest;
import com.example.ledgerlift.features.organization.dto.OrganizationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final MailService mailService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userUuid}")
    public void createOrganization(@PathVariable String userUuid,
                                   @Valid @RequestBody OrganizationRequest request) {

        organizationService.createOrganization(userUuid, request);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<OrganizationResponse> getAll() {

        return organizationService.getAll();

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userUuid}")
    public List<OrganizationResponse> getAll(@PathVariable String userUuid) {

        return organizationService.getOrganizationsByUserUuid(userUuid);

    }

    @GetMapping("/get-pending-organizations")
    public List<OrganizationResponse> getPendingOrganizations() {

        return organizationService.getPendingOrganization();

    }

    @PutMapping("/{organizationUuid}/upload-qr")
    public void uploadMoneyQrCode(@PathVariable String organizationUuid,
                                  @Valid @RequestBody ImageRequest qrImage) {

        organizationService.uploadQrImage(organizationUuid, qrImage);

    }

    @PutMapping("/{organizationUuid}")
    public BasedMessage updateOrganization(@PathVariable String organizationUuid,
                                           @RequestBody OrganizationRequest request) {

        organizationService.updateOrganizationByUuid(organizationUuid, request);

        return BasedMessage.builder()
                .message("Organization updated successfully")
                .build();

    }

    @PutMapping("/{organizationUuid}/lock")
    BasedMessage lockOrganization(@PathVariable String organizationUuid) {

        organizationService.lockOrganization(organizationUuid);

        return new BasedMessage("Organization has been locked");

    }

    @PutMapping("/{organizationUuid}/approve-org")
    BasedMessage approveOrganization(@PathVariable String organizationUuid) {

        organizationService.approveOrg(organizationUuid);

        return new BasedMessage("Organization has been approved");

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{organizationUuid}")
    public BasedMessage deleteOrganization(@PathVariable String organizationUuid) {

        organizationService.deleteOrganizationByUuid(organizationUuid);

        return BasedMessage.builder()
                .message("Organization deleted successfully")
                .build();

    }

}
