package com.example.modulith.services.employee;

// 向外界传输的使用的 DTO，相当于原有的 request 和 response，用于传输数据
// 这里已经不需要再抽取到其他的 package 了，因为这个 DTO 只会在当前业务模块的 package 中使用
public record EmployeeDTO(Long id,
                          Long organizationId,
                          Long departmentId,
                          String name,
                          int age,
                          String position) {
}
