package com.lejingling.modulith.services.employee.management;

import com.lejingling.modulith.OrganizationRemoveEvent;
import com.lejingling.modulith.services.employee.EmployeeDTO;
import com.lejingling.modulith.services.employee.EmployeeExternalAPI;
import com.lejingling.modulith.services.employee.EmployeeInternalAPI;
import com.lejingling.modulith.services.employee.mapper.EmployeeMapper;
import com.lejingling.modulith.services.employee.model.Employee;
import com.lejingling.modulith.services.employee.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeManagement implements EmployeeInternalAPI,
        EmployeeExternalAPI {

    private static final Logger LOG = LoggerFactory
            .getLogger(EmployeeManagement.class);
    private EmployeeRepository repository;
    private EmployeeMapper mapper;

    public EmployeeManagement(EmployeeRepository repository,
                              EmployeeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<EmployeeDTO> getEmployeesByDepartmentId(Long departmentId) {
        return repository.findByDepartmentId(departmentId);
    }

    @Override
    public List<EmployeeDTO> getEmployeesByOrganizationId(Long id) {
        return repository.findByOrganizationId(id);
    }

    @Override
    @Transactional
    public EmployeeDTO add(EmployeeDTO employee) {
        Employee emp = mapper.employeeDTOToEmployee(employee);
        return mapper.employeeToEmployeeDTO(repository.save(emp));
    }

    @ApplicationModuleListener
    void onRemovedOrganizationEvent(OrganizationRemoveEvent event) {
        LOG.info("onRemovedOrganizationEvent(orgId={})", event.getId());
        repository.deleteByOrganizationId(event.getId());
    }

}