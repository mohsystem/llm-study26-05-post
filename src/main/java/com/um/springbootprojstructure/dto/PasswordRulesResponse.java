package com.um.springbootprojstructure.dto;

import java.time.Instant;

public class PasswordRulesResponse {

    private int minLength;

    private boolean requireUppercase;
    private boolean requireLowercase;
    private boolean requireDigit;
    private boolean requireSpecial;

    private int minSpecial;
    private int minDigits;
    private int minUppercase;
    private int minLowercase;

    private boolean disallowPersonalInfo;

    private Instant updatedAt;

    public int getMinLength() { return minLength; }
    public void setMinLength(int minLength) { this.minLength = minLength; }

    public boolean isRequireUppercase() { return requireUppercase; }
    public void setRequireUppercase(boolean requireUppercase) { this.requireUppercase = requireUppercase; }

    public boolean isRequireLowercase() { return requireLowercase; }
    public void setRequireLowercase(boolean requireLowercase) { this.requireLowercase = requireLowercase; }

    public boolean isRequireDigit() { return requireDigit; }
    public void setRequireDigit(boolean requireDigit) { this.requireDigit = requireDigit; }

    public boolean isRequireSpecial() { return requireSpecial; }
    public void setRequireSpecial(boolean requireSpecial) { this.requireSpecial = requireSpecial; }

    public int getMinSpecial() { return minSpecial; }
    public void setMinSpecial(int minSpecial) { this.minSpecial = minSpecial; }

    public int getMinDigits() { return minDigits; }
    public void setMinDigits(int minDigits) { this.minDigits = minDigits; }

    public int getMinUppercase() { return minUppercase; }
    public void setMinUppercase(int minUppercase) { this.minUppercase = minUppercase; }

    public int getMinLowercase() { return minLowercase; }
    public void setMinLowercase(int minLowercase) { this.minLowercase = minLowercase; }

    public boolean isDisallowPersonalInfo() { return disallowPersonalInfo; }
    public void setDisallowPersonalInfo(boolean disallowPersonalInfo) { this.disallowPersonalInfo = disallowPersonalInfo; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
