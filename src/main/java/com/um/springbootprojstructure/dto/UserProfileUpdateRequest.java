package com.um.springbootprojstructure.dto;

import jakarta.validation.constraints.Size;

public class UserProfileUpdateRequest {

    @Size(max = 60)
    private String firstName;

    @Size(max = 60)
    private String lastName;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
