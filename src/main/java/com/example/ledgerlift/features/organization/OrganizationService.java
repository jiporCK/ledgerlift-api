package com.example.ledgerlift.features.organization;

import com.example.ledgerlift.domain.Organization;
import com.example.ledgerlift.features.media.dto.ImageRequest;
import com.example.ledgerlift.features.organization.dto.OrganizationRequest;
import com.example.ledgerlift.features.organization.dto.OrganizationResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface OrganizationService {

    void createOrganization(String userUuid, OrganizationRequest request);

    OrganizationResponse getOrganizationByUuid(String uuid);

    List<OrganizationResponse> getOrganizationsByUserUuid(String userUuid);

    List<OrganizationResponse> getAll();

    void deleteOrganizationByUuid(String uuid);

    void updateOrganizationByUuid(String uuid, OrganizationRequest request);

    void uploadQrImage(String organizationUuid, @Valid ImageRequest qrImage);

    void lockOrganization(String organizationUuid);
}
