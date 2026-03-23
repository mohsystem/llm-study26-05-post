package com.um.springbootprojstructure.mapper;

import com.um.springbootprojstructure.dto.PasswordRulesRequest;
import com.um.springbootprojstructure.dto.PasswordRulesResponse;
import com.um.springbootprojstructure.entity.PasswordRules;
import org.springframework.stereotype.Component;

@Component
public class PasswordRulesMapper {

    public PasswordRulesResponse toResponse(PasswordRules e) {
        PasswordRulesResponse r = new PasswordRulesResponse();
        r.setMinLength(e.getMinLength());

        r.setRequireUppercase(e.isRequireUppercase());
        r.setRequireLowercase(e.isRequireLowercase());
        r.setRequireDigit(e.isRequireDigit());
        r.setRequireSpecial(e.isRequireSpecial());

        r.setMinSpecial(e.getMinSpecial());
        r.setMinDigits(e.getMinDigits());
        r.setMinUppercase(e.getMinUppercase());
        r.setMinLowercase(e.getMinLowercase());

        r.setDisallowPersonalInfo(e.isDisallowPersonalInfo());
        r.setUpdatedAt(e.getUpdatedAt());
        return r;
    }

    public void applyRequest(PasswordRules e, PasswordRulesRequest req) {
        e.setMinLength(req.getMinLength());

        e.setRequireUppercase(req.isRequireUppercase());
        e.setRequireLowercase(req.isRequireLowercase());
        e.setRequireDigit(req.isRequireDigit());
        e.setRequireSpecial(req.isRequireSpecial());

        e.setMinSpecial(req.getMinSpecial());
        e.setMinDigits(req.getMinDigits());
        e.setMinUppercase(req.getMinUppercase());
        e.setMinLowercase(req.getMinLowercase());

        e.setDisallowPersonalInfo(req.isDisallowPersonalInfo());
    }
}
