package com.example.modulith.services.organization;

import org.jmolecules.event.annotation.Externalized;

@Externalized("organization-remove::#{#this.getId()}")
public class OrganizationRemoveEvent {
    Long id;

    public OrganizationRemoveEvent(Long id) {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
