package com.lejingling.modulith;

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
