package com.example.modulith.services.department.adapter;

import com.example.modulith.services.department.DepartmentDTO;

// adapter 相当于是原有的 Controller
public interface DepartmentExternalAPI {
    DepartmentDTO getDepartmentByIdWithEmployees(Long id);

    DepartmentDTO add(DepartmentDTO department);
}
