package com.example.modulith.services.department.adapter;

import com.example.modulith.services.department.DepartmentDTO;

import java.util.List;

public interface DepartmentInternalAPI {
    List<DepartmentDTO> getDepartmentsByOrganizationId(Long id);

    List<DepartmentDTO> getDepartmentsByOrganizationIdWithEmployees(Long id);
}
