package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.PasswordRulesRequest;
import com.um.springbootprojstructure.dto.PasswordRulesResponse;

public interface PasswordRulesService {
    PasswordRulesResponse getActiveRules();
    PasswordRulesResponse updateRules(PasswordRulesRequest request);
}
