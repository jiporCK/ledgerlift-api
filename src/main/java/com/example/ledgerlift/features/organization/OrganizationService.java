package com.example.ledgerlift.features.organization;

import com.example.ledgerlift.domain.Organization;
import com.example.ledgerlift.features.organization.dto.OrganizationRequest;
import com.example.ledgerlift.features.organization.dto.OrganizationResponse;

import java.util.List;

public interface OrganizationService {

    void createOrganization(String userUuid, OrganizationRequest request);

    OrganizationResponse getOrganizationByUuid(String uuid);

    List<OrganizationResponse> getAll();

    void deleteOrganizationByUuid(String uuid);

    void updateOrganizationByUuid(String uuid, Organization organization);

}
