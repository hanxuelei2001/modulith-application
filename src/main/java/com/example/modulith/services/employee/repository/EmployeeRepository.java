package com.example.modulith.services.employee.repository;

import com.example.modulith.services.employee.EmployeeDTO;
import com.example.modulith.services.employee.model.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    List<EmployeeDTO> findByDepartmentId(Long departmentId);

    List<EmployeeDTO> findByOrganizationId(Long organizationId);

    void deleteByOrganizationId(Long organizationId);
}