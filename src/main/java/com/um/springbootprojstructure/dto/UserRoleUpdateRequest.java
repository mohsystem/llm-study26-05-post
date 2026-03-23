package com.um.springbootprojstructure.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public class UserRoleUpdateRequest {

    /**
     * Example: ["USER"] or ["ADMIN","USER"]
     * Interpreted as a full replacement of roles.
     */
    @NotEmpty
    private Set<String> roles;

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}
