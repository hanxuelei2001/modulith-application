package com.lejingling.modulith.services.department.mapper;

import com.lejingling.modulith.services.department.DepartmentDTO;
import com.lejingling.modulith.services.department.model.Department;
import com.lejingling.modulith.services.employee.EmployeeDTO;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-15T12:03:06+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class DepartmentMapperImpl implements DepartmentMapper {

    @Override
    public DepartmentDTO departmentToEmployeeDTO(Department department) {
        if ( department == null ) {
            return null;
        }

        Long id = null;
        Long organizationId = null;
        String name = null;

        id = department.getId();
        organizationId = department.getOrganizationId();
        name = department.getName();

        List<EmployeeDTO> employees = null;

        DepartmentDTO departmentDTO = new DepartmentDTO( id, organizationId, name, employees );

        return departmentDTO;
    }

    @Override
    public Department departmentDTOToEmployee(DepartmentDTO departmentDTO) {
        if ( departmentDTO == null ) {
            return null;
        }

        Department department = new Department();

        department.setId( departmentDTO.id() );
        department.setOrganizationId( departmentDTO.organizationId() );
        department.setName( departmentDTO.name() );

        return department;
    }
}
