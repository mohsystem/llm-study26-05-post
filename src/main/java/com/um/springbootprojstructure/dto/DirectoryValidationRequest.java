package com.um.springbootprojstructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class DirectoryValidationRequest {

    /**
     * Provide any combination of these. At least one must be present.
     */
    @Email
    @Size(max = 180)
    private String email;

    @Size(max = 100)
    private String username;

    @Size(max = 60)
    private String firstName;

    @Size(max = 60)
    private String lastName;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
