package com.example.modulith.services.organization.management;

import com.example.modulith.services.event.OrganizationAddEvent;
import com.example.modulith.services.event.OrganizationRemoveEvent;
import com.example.modulith.services.department.DepartmentDTO;
import com.example.modulith.services.department.DepartmentInternalAPI;
import com.example.modulith.services.employee.EmployeeDTO;
import com.example.modulith.services.employee.EmployeeInternalAPI;
import com.example.modulith.services.organization.OrganizationDTO;
import com.example.modulith.services.organization.OrganizationExternalAPI;
import com.example.modulith.services.organization.mapper.OrganizationMapper;
import com.example.modulith.services.organization.repository.OrganizationRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrganizationManagement implements OrganizationExternalAPI {

    private final ApplicationEventPublisher events;
    private final OrganizationRepository repository;
    private final DepartmentInternalAPI departmentInternalAPI;
    private final EmployeeInternalAPI employeeInternalAPI;
    private final OrganizationMapper mapper;

    public OrganizationManagement(ApplicationEventPublisher events,
                                  OrganizationRepository repository,
                                  DepartmentInternalAPI departmentInternalAPI,
                                  EmployeeInternalAPI employeeInternalAPI,
                                  OrganizationMapper mapper) {
        this.events = events;
        this.repository = repository;
        this.departmentInternalAPI = departmentInternalAPI;
        this.employeeInternalAPI = employeeInternalAPI;
        this.mapper = mapper;
    }

    @Override
    public OrganizationDTO findByIdWithEmployees(Long id) {
        OrganizationDTO dto = repository.findDTOById(id);
        List<EmployeeDTO> dtos = employeeInternalAPI.getEmployeesByOrganizationId(id);
        dto.employees().addAll(dtos);
        return dto;
    }

    @Override
    public OrganizationDTO findByIdWithDepartments(Long id) {
        OrganizationDTO dto = repository.findDTOById(id);
        List<DepartmentDTO> dtos = departmentInternalAPI.getDepartmentsByOrganizationId(id);
        dto.departments().addAll(dtos);
        return dto;
    }

    @Override
    public OrganizationDTO findByIdWithDepartmentsAndEmployees(Long id) {
        OrganizationDTO dto = repository.findDTOById(id);
        List<DepartmentDTO> dtos = departmentInternalAPI.getDepartmentsByOrganizationIdWithEmployees(id);
        dto.departments().addAll(dtos);
        return dto;
    }

    @Override
    @Transactional
    public OrganizationDTO add(OrganizationDTO organization) {
        OrganizationDTO dto = mapper.organizationToOrganizationDTO(
                repository.save(mapper.organizationDTOToOrganization(organization))
        );
        events.publishEvent(new OrganizationAddEvent(dto.id()));
        return dto;
    }

    @Override
    @Transactional
    public void remove(Long id) {
        repository.deleteById(id);
        events.publishEvent(new OrganizationRemoveEvent(id));
    }

}