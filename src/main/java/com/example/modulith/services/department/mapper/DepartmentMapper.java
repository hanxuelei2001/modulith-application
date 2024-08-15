package com.example.modulith.services.department.mapper;

import com.example.modulith.services.department.DepartmentDTO;
import com.example.modulith.services.department.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

// 这里的 @Mapper 注解是 MapStruct 的注解，用于生成 DTO 和 Entity 之间的映射关系
// 负责将 Entity 转换为 DTO
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DepartmentMapper {
    DepartmentDTO departmentToEmployeeDTO(Department department);

    Department departmentDTOToEmployee(DepartmentDTO departmentDTO);
}