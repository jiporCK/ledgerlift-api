package com.example.ledgerlift.features.organization;

import com.example.ledgerlift.domain.Organization;
import com.example.ledgerlift.features.mail.MailService;
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

}
