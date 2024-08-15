package com.example.modulith.services.department.management;

import com.example.modulith.services.organization.OrganizationAddEvent;
import com.example.modulith.services.organization.OrganizationRemoveEvent;
import com.example.modulith.services.department.DepartmentDTO;
import com.example.modulith.services.department.DepartmentExternalAPI;
import com.example.modulith.services.department.DepartmentInternalAPI;
import com.example.modulith.services.department.mapper.DepartmentMapper;
import com.example.modulith.services.department.repository.DepartmentRepository;
import com.example.modulith.services.employee.EmployeeDTO;
import com.example.modulith.services.employee.EmployeeInternalAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentManagement implements DepartmentInternalAPI, DepartmentExternalAPI {

    private static final Logger LOG = LoggerFactory
            .getLogger(DepartmentManagement.class);
    private DepartmentRepository repository;
    private EmployeeInternalAPI employeeInternalAPI;
    private DepartmentMapper mapper;

    public DepartmentManagement(DepartmentRepository repository,
                                EmployeeInternalAPI employeeInternalAPI,
                                DepartmentMapper mapper) {
        this.repository = repository;
        this.employeeInternalAPI = employeeInternalAPI;
        this.mapper = mapper;
    }

    @Override
    public DepartmentDTO getDepartmentByIdWithEmployees(Long id) {
        DepartmentDTO d = repository.findDTOById(id);
        List<EmployeeDTO> dtos = employeeInternalAPI
                .getEmployeesByDepartmentId(id);
        d.employees().addAll(dtos);
        return d;
    }

    @EventListener
    void onNewOrganizationEvent(OrganizationAddEvent event) {
        LOG.info("onNewOrganizationEvent(orgId={})", event.getId());
        add(new DepartmentDTO(null, event.getId(), "HR"));
        add(new DepartmentDTO(null, event.getId(), "Management"));
    }

    @EventListener
    void onRemovedOrganizationEvent(OrganizationRemoveEvent event) {
        LOG.info("onRemovedOrganizationEvent(orgId={})", event.getId());
        repository.deleteByOrganizationId(event.getId());
    }

    @Override
    public DepartmentDTO add(DepartmentDTO department) {
        return mapper.departmentToEmployeeDTO(
                repository.save(mapper.departmentDTOToEmployee(department))
        );
    }

    @Override
    public List<DepartmentDTO> getDepartmentsByOrganizationId(Long id) {
        return repository.findByOrganizationId(id);
    }

    @Override
    public List<DepartmentDTO> getDepartmentsByOrganizationIdWithEmployees(Long id) {
        return null;
    }
}