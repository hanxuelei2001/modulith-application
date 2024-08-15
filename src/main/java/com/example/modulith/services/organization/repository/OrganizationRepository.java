package com.example.modulith.services.organization.repository;

import com.example.modulith.services.organization.OrganizationDTO;
import com.example.modulith.services.organization.model.Organization;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface OrganizationRepository extends CrudRepository<Organization, Long> {

    @Query("""
            SELECT new com.example.modulith.services.organization.OrganizationDTO(o.id, o.name, o.address)
            FROM Organization o
            WHERE o.id = :id
            """)
    OrganizationDTO findDTOById(@Param("id") Long id);
}