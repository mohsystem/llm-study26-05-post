package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.PasswordPolicyResultResponse;
import com.um.springbootprojstructure.entity.User;
import java.util.List;

public interface PasswordPolicyValidator {

    /**
     * Validates raw password against active rules.
     * userContext can be null when not available.
     */
    PasswordPolicyResultResponse validate(String rawPassword, User userContext);

    /**
     * Deterministic reason codes.
     */
    enum Reason {
        MIN_LENGTH,
        REQUIRE_UPPERCASE,
        REQUIRE_LOWERCASE,
        REQUIRE_DIGIT,
        REQUIRE_SPECIAL,
        MIN_UPPERCASE,
        MIN_LOWERCASE,
        MIN_DIGITS,
        MIN_SPECIAL,
        DISALLOW_PERSONAL_INFO
    }

    static List<String> toCodes(List<Reason> reasons) {
        return reasons.stream().map(Enum::name).sorted().toList();
    }
}
