package com.um.springbootprojstructure.dto;

import java.util.Set;

public class UserMergeResultResponse {

    private String sourcePublicRef;
    private String targetPublicRef;

    private boolean sourceDeactivated;

    private Set<String> rolesAddedToTarget;

    private boolean firstNameChanged;
    private boolean lastNameChanged;
    private boolean emailChanged;

    private String message;

    public String getSourcePublicRef() { return sourcePublicRef; }
    public void setSourcePublicRef(String sourcePublicRef) { this.sourcePublicRef = sourcePublicRef; }

    public String getTargetPublicRef() { return targetPublicRef; }
    public void setTargetPublicRef(String targetPublicRef) { this.targetPublicRef = targetPublicRef; }

    public boolean isSourceDeactivated() { return sourceDeactivated; }
    public void setSourceDeactivated(boolean sourceDeactivated) { this.sourceDeactivated = sourceDeactivated; }

    public Set<String> getRolesAddedToTarget() { return rolesAddedToTarget; }
    public void setRolesAddedToTarget(Set<String> rolesAddedToTarget) { this.rolesAddedToTarget = rolesAddedToTarget; }

    public boolean isFirstNameChanged() { return firstNameChanged; }
    public void setFirstNameChanged(boolean firstNameChanged) { this.firstNameChanged = firstNameChanged; }

    public boolean isLastNameChanged() { return lastNameChanged; }
    public void setLastNameChanged(boolean lastNameChanged) { this.lastNameChanged = lastNameChanged; }

    public boolean isEmailChanged() { return emailChanged; }
    public void setEmailChanged(boolean emailChanged) { this.emailChanged = emailChanged; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
