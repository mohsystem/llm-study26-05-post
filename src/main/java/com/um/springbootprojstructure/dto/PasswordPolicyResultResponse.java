package com.um.springbootprojstructure.dto;

import java.util.ArrayList;
import java.util.List;

public class PasswordPolicyResultResponse {
    private boolean accepted;
    private List<String> reasons = new ArrayList<>();
    private String message;

    public PasswordPolicyResultResponse() {}

    public PasswordPolicyResultResponse(boolean accepted, List<String> reasons, String message) {
        this.accepted = accepted;
        this.reasons = reasons;
        this.message = message;
    }

    public boolean isAccepted() { return accepted; }
    public void setAccepted(boolean accepted) { this.accepted = accepted; }

    public List<String> getReasons() { return reasons; }
    public void setReasons(List<String> reasons) { this.reasons = reasons; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static PasswordPolicyResultResponse accepted() {
        return new PasswordPolicyResultResponse(true, List.of(), "Password accepted");
    }

    public static PasswordPolicyResultResponse rejected(List<String> reasons) {
        return new PasswordPolicyResultResponse(false, reasons, "Password rejected");
    }
}
