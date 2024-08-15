package com.example.modulith.services.organization;

import com.example.modulith.services.department.DepartmentDTO;
import com.example.modulith.services.employee.EmployeeDTO;

import java.util.ArrayList;
import java.util.List;

public record OrganizationDTO(Long id,
                              String name,
                              String address,
                              List<DepartmentDTO> departments,
                              List<EmployeeDTO> employees) {
    public OrganizationDTO(Long id, String name, String address) {
        this(id, name, address, new ArrayList<>(), new ArrayList<>());
    }
}