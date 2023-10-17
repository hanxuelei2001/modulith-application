package com.lejingling.modulith.services.gateway;

import com.lejingling.modulith.services.department.DepartmentDTO;
import com.lejingling.modulith.services.department.DepartmentExternalAPI;
import com.lejingling.modulith.services.employee.EmployeeDTO;
import com.lejingling.modulith.services.employee.EmployeeExternalAPI;
import com.lejingling.modulith.services.organization.OrganizationDTO;
import com.lejingling.modulith.services.organization.OrganizationExternalAPI;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GatewayManagement {

    private DepartmentExternalAPI departmentExternalAPI;
    private EmployeeExternalAPI employeeExternalAPI;
    private OrganizationExternalAPI organizationExternalAPI;

    public GatewayManagement(DepartmentExternalAPI departmentExternalAPI,
                             EmployeeExternalAPI employeeExternalAPI,
                             OrganizationExternalAPI organizationExternalAPI) {
        this.departmentExternalAPI = departmentExternalAPI;
        this.employeeExternalAPI = employeeExternalAPI;
        this.organizationExternalAPI = organizationExternalAPI;
    }


    @GetMapping("/organizations/{id}/with-departments")
    public OrganizationDTO apiOrganizationWithDepartments(@PathVariable("id") Long id) {
        return organizationExternalAPI.findByIdWithDepartments(id);
    }

    @GetMapping("/organizations/{id}/with-departments-and-employees")
    public OrganizationDTO apiOrganizationWithDepartmentsAndEmployees(@PathVariable("id") Long id) {
        return organizationExternalAPI.findByIdWithDepartmentsAndEmployees(id);
    }

    @PostMapping("/organizations")
    public OrganizationDTO apiAddOrganization(@RequestBody OrganizationDTO o) {
        return organizationExternalAPI.add(o);
    }

    @PostMapping("/employees")
    public EmployeeDTO apiAddEmployee(@RequestBody EmployeeDTO employee) {
        return employeeExternalAPI.add(employee);
    }

    @GetMapping("/departments/{id}/with-employees")
    public DepartmentDTO apiDepartmentWithEmployees(@PathVariable("id") Long id) {
        return departmentExternalAPI.getDepartmentByIdWithEmployees(id);
    }

    @PostMapping("/departments")
    public DepartmentDTO apiAddDepartment(@RequestBody DepartmentDTO department) {
        return departmentExternalAPI.add(department);
    }
}