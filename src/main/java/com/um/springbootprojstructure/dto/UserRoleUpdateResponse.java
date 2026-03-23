package com.um.springbootprojstructure.dto;

import java.util.Set;

public class UserRoleUpdateResponse {
    private Long id;
    private String publicRef;
    private Set<String> roles;

    public UserRoleUpdateResponse() {}

    public UserRoleUpdateResponse(Long id, String publicRef, Set<String> roles) {
        this.id = id;
        this.publicRef = publicRef;
        this.roles = roles;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPublicRef() { return publicRef; }
    public void setPublicRef(String publicRef) { this.publicRef = publicRef; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}
