package com.lejingling.modulith.services.employee.mapper;

import com.lejingling.modulith.services.employee.EmployeeDTO;
import com.lejingling.modulith.services.employee.model.Employee;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-15T12:03:06+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public EmployeeDTO employeeToEmployeeDTO(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        Long id = null;
        Long organizationId = null;
        Long departmentId = null;
        String name = null;
        int age = 0;
        String position = null;

        id = employee.getId();
        organizationId = employee.getOrganizationId();
        departmentId = employee.getDepartmentId();
        name = employee.getName();
        age = employee.getAge();
        position = employee.getPosition();

        EmployeeDTO employeeDTO = new EmployeeDTO( id, organizationId, departmentId, name, age, position );

        return employeeDTO;
    }

    @Override
    public Employee employeeDTOToEmployee(EmployeeDTO employeeDTO) {
        if ( employeeDTO == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setId( employeeDTO.id() );
        employee.setOrganizationId( employeeDTO.organizationId() );
        employee.setDepartmentId( employeeDTO.departmentId() );
        employee.setName( employeeDTO.name() );
        employee.setAge( employeeDTO.age() );
        employee.setPosition( employeeDTO.position() );

        return employee;
    }
}
