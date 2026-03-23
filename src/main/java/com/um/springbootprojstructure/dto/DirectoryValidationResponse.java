package com.um.springbootprojstructure.dto;

import java.util.Map;

public class DirectoryValidationResponse {

    public enum Status {
        MATCHED,
        NOT_FOUND,
        AMBIGUOUS,
        ERROR
    }

    private Status status;
    private String dn;
    private Map<String, Object> attributes;
    private String message;

    public DirectoryValidationResponse() {}

    public DirectoryValidationResponse(Status status, String dn, Map<String, Object> attributes, String message) {
        this.status = status;
        this.dn = dn;
        this.attributes = attributes;
        this.message = message;
    }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getDn() { return dn; }
    public void setDn(String dn) { this.dn = dn; }

    public Map<String, Object> getAttributes() { return attributes; }
    public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
