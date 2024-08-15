package com.example.modulith.services.employee;

import com.example.modulith.services.event.OrganizationRemoveEvent;
import com.example.modulith.services.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

@ApplicationModuleTest(ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeModuleTests {

    @Autowired
    EmployeeRepository repository;

    @Test
    @Order(1)
    void shouldRemoveEmployeesOnEvent(Scenario scenario) {
        scenario.publish(new OrganizationRemoveEvent(1L))
                .andWaitForStateChange(() -> repository.count())
                .andVerify(result -> {
                    assert result.intValue() == 0;
                });
    }
}