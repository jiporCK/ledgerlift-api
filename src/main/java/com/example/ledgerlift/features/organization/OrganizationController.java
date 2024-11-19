package com.example.ledgerlift.features.organization;

import com.example.ledgerlift.features.organization.dto.OrganizationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userUuid}")
    public void createOrganization(@PathVariable String userUuid,
                                   @RequestBody OrganizationRequest request) {

        organizationService.createOrganization(userUuid, request);

    }

}
