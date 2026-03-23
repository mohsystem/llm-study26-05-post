package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.PasswordPolicyResultResponse;
import com.um.springbootprojstructure.entity.PasswordRules;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.repository.PasswordRulesRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordPolicyValidatorImpl implements PasswordPolicyValidator {

    private final PasswordRulesRepository rulesRepository;

    public PasswordPolicyValidatorImpl(PasswordRulesRepository rulesRepository) {
        this.rulesRepository = rulesRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PasswordPolicyResultResponse validate(String rawPassword, User userContext) {
        PasswordRules rules = rulesRepository.findById(1L).orElseGet(PasswordRules::new);

        List<Reason> reasons = new ArrayList<>();

        if (rawPassword == null) rawPassword = "";
        int len = rawPassword.length();

        if (len < rules.getMinLength()) reasons.add(Reason.MIN_LENGTH);

        int upper = 0, lower = 0, digit = 0, special = 0;
        for (int i = 0; i < rawPassword.length(); i++) {
            char c = rawPassword.charAt(i);
            if (Character.isUpperCase(c)) upper++;
            else if (Character.isLowerCase(c)) lower++;
            else if (Character.isDigit(c)) digit++;
            else special++;
        }

        if (rules.isRequireUppercase() && upper == 0) reasons.add(Reason.REQUIRE_UPPERCASE);
        if (rules.isRequireLowercase() && lower == 0) reasons.add(Reason.REQUIRE_LOWERCASE);
        if (rules.isRequireDigit() && digit == 0) reasons.add(Reason.REQUIRE_DIGIT);
        if (rules.isRequireSpecial() && special == 0) reasons.add(Reason.REQUIRE_SPECIAL);

        if (upper < rules.getMinUppercase()) reasons.add(Reason.MIN_UPPERCASE);
        if (lower < rules.getMinLowercase()) reasons.add(Reason.MIN_LOWERCASE);
        if (digit < rules.getMinDigits()) reasons.add(Reason.MIN_DIGITS);
        if (special < rules.getMinSpecial()) reasons.add(Reason.MIN_SPECIAL);

        if (rules.isDisallowPersonalInfo() && userContext != null) {
            if (containsPersonalInfo(rawPassword, userContext)) {
                reasons.add(Reason.DISALLOW_PERSONAL_INFO);
            }
        }

        List<String> codes = PasswordPolicyValidator.toCodes(reasons);

        if (codes.isEmpty()) return PasswordPolicyResultResponse.accepted();
        return PasswordPolicyResultResponse.rejected(codes);
    }

    private boolean containsPersonalInfo(String password, User user) {
        String pw = password.toLowerCase(Locale.ROOT);

        if (user.getFirstName() != null && !user.getFirstName().isBlank()) {
            if (pw.contains(user.getFirstName().toLowerCase(Locale.ROOT))) return true;
        }
        if (user.getLastName() != null && !user.getLastName().isBlank()) {
            if (pw.contains(user.getLastName().toLowerCase(Locale.ROOT))) return true;
        }
        if (user.getEmail() != null && user.getEmail().contains("@")) {
            String local = user.getEmail().substring(0, user.getEmail().indexOf('@'));
            if (!local.isBlank() && pw.contains(local.toLowerCase(Locale.ROOT))) return true;
        }
        return false;
    }
}
