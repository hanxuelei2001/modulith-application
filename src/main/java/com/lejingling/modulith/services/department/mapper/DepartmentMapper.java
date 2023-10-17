package com.lejingling.modulith.services.department.mapper;

import com.lejingling.modulith.services.department.DepartmentDTO;
import com.lejingling.modulith.services.department.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DepartmentMapper {
    DepartmentDTO departmentToEmployeeDTO(Department department);

    Department departmentDTOToEmployee(DepartmentDTO departmentDTO);
}