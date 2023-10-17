package com.lejingling.modulith.services.department.repository;

import com.lejingling.modulith.services.department.DepartmentDTO;
import com.lejingling.modulith.services.department.model.Department;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentRepository extends CrudRepository<Department, Long> {

    @Query("""
            SELECT new com.lejingling.modulith.services.department.DepartmentDTO(d.id, d.organizationId, d.name)
            FROM Department d
            WHERE d.id = :id
            """)
    DepartmentDTO findDTOById(@Param("id") Long id);

    @Query("""
            SELECT new com.lejingling.modulith.services.department.DepartmentDTO(d.id, d.organizationId, d.name)
            FROM Department d
            WHERE d.organizationId = :organizationId
            """)
    List<DepartmentDTO> findByOrganizationId(@Param("organizationId") Long organizationId);

    void deleteByOrganizationId(Long organizationId);
}