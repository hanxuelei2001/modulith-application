package com.lejingling.modulith.services.organization.mapper;

import com.lejingling.modulith.services.department.DepartmentDTO;
import com.lejingling.modulith.services.employee.EmployeeDTO;
import com.lejingling.modulith.services.organization.OrganizationDTO;
import com.lejingling.modulith.services.organization.model.Organization;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-15T12:03:06+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class OrganizationMapperImpl implements OrganizationMapper {

    @Override
    public OrganizationDTO organizationToOrganizationDTO(Organization organization) {
        if ( organization == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String address = null;

        id = organization.getId();
        name = organization.getName();
        address = organization.getAddress();

        List<DepartmentDTO> departments = null;
        List<EmployeeDTO> employees = null;

        OrganizationDTO organizationDTO = new OrganizationDTO( id, name, address, departments, employees );

        return organizationDTO;
    }

    @Override
    public Organization organizationDTOToOrganization(OrganizationDTO organizationDTO) {
        if ( organizationDTO == null ) {
            return null;
        }

        Organization organization = new Organization();

        organization.setId( organizationDTO.id() );
        organization.setName( organizationDTO.name() );
        organization.setAddress( organizationDTO.address() );

        return organization;
    }
}
