package com.example.modulith.services.employee.mapper;

import com.example.modulith.services.employee.EmployeeDTO;
import com.example.modulith.services.employee.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper {
    EmployeeDTO employeeToEmployeeDTO(Employee employee);

    Employee employeeDTOToEmployee(EmployeeDTO employeeDTO);
}