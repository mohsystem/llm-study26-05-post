package com.um.springbootprojstructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "password_rules")
public class PasswordRules {

    @Id
    private Long id = 1L;

    @Column(nullable = false)
    private int minLength = 12;

    @Column(nullable = false)
    private boolean requireUppercase = true;

    @Column(nullable = false)
    private boolean requireLowercase = true;

    @Column(nullable = false)
    private boolean requireDigit = true;

    @Column(nullable = false)
    private boolean requireSpecial = true;

    @Column(nullable = false)
    private int minSpecial = 1;

    @Column(nullable = false)
    private int minDigits = 1;

    @Column(nullable = false)
    private int minUppercase = 1;

    @Column(nullable = false)
    private int minLowercase = 1;

    /**
     * If true, password must not contain the email local-part or first/last name.
     * (Actual enforcement will be done in validation service later.)
     */
    @Column(nullable = false)
    private boolean disallowPersonalInfo = true;

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
