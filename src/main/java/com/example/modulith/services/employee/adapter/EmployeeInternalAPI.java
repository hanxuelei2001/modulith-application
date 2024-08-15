package com.example.modulith.services.employee.adapter;

import com.example.modulith.services.employee.EmployeeDTO;

import java.util.List;

public interface EmployeeInternalAPI {
    List<EmployeeDTO> getEmployeesByDepartmentId(Long id);

    List<EmployeeDTO> getEmployeesByOrganizationId(Long id);
}
