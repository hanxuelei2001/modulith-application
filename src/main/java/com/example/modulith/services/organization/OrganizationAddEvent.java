package com.example.modulith.services.organization;

import org.jmolecules.event.annotation.Externalized;

@Externalized("organization-add::#{#this.getId()}")
public class OrganizationAddEvent {
    Long Id;

    public OrganizationAddEvent(Long id) {
        Id = id;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
