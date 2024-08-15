package com.example.modulith.services.organization.adapter;

import com.example.modulith.services.organization.OrganizationDTO;

public interface OrganizationExternalAPI {

    OrganizationDTO findByIdWithEmployees(Long id);

    OrganizationDTO findByIdWithDepartments(Long id);

    OrganizationDTO findByIdWithDepartmentsAndEmployees(Long id);

    OrganizationDTO add(OrganizationDTO organization);

    void remove(Long id);

}