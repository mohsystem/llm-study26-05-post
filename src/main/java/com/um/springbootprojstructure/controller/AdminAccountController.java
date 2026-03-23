package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.PasswordRulesRequest;
import com.um.springbootprojstructure.dto.PasswordRulesResponse;
import com.um.springbootprojstructure.service.PasswordRulesService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/accounts")
public class AdminAccountController {

    private final PasswordRulesService passwordRulesService;

    public AdminAccountController(PasswordRulesService passwordRulesService) {
        this.passwordRulesService = passwordRulesService;
    }

    @GetMapping("/password-rules")
    public PasswordRulesResponse getPasswordRules() {
        return passwordRulesService.getActiveRules();
    }

    @PutMapping("/password-rules")
    public PasswordRulesResponse updatePasswordRules(@Valid @RequestBody PasswordRulesRequest request) {
        return passwordRulesService.updateRules(request);
    }
}
