package com.example.modulith.services.department;

public interface DepartmentExternalAPI {
    DepartmentDTO getDepartmentByIdWithEmployees(Long id);

    DepartmentDTO add(DepartmentDTO department);
}
