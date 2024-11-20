package com.example.ledgerlift.mapper;

import com.example.ledgerlift.domain.Organization;
import com.example.ledgerlift.features.organization.dto.OrganizationRequest;
import com.example.ledgerlift.features.organization.dto.OrganizationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    Organization fromOrganizationRequest(OrganizationRequest organizationRequest);

    @Mapping(target = "user", source = "user")
    OrganizationResponse toOrganizationResponse(Organization organization);

}
