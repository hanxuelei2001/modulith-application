package com.example.modulith.services.organization.mapper;

import com.example.modulith.services.organization.OrganizationDTO;
import com.example.modulith.services.organization.model.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrganizationMapper {
    OrganizationDTO organizationToOrganizationDTO(Organization organization);

    Organization organizationDTOToOrganization(OrganizationDTO organizationDTO);
}