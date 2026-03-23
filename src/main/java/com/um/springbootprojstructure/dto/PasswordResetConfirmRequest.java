package com.um.springbootprojstructure.dto;

import jakarta.validation.constraints.NotBlank;

public class PasswordResetConfirmRequest {
    @NotBlank
    private String resetToken;

    @NotBlank
    private String newPassword;

    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
