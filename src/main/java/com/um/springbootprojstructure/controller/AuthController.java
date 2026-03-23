package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.AuthRegisterRequest;
import com.um.springbootprojstructure.dto.AuthLoginRequest;
import com.um.springbootprojstructure.dto.AuthRefreshRequest;
import com.um.springbootprojstructure.dto.AuthTokenResponse;
import com.um.springbootprojstructure.dto.LogoutResponse;
import com.um.springbootprojstructure.dto.PasswordChangeRequest;
import com.um.springbootprojstructure.dto.PasswordPolicyResultResponse;
import com.um.springbootprojstructure.dto.PasswordResetConfirmRequest;
import com.um.springbootprojstructure.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public PasswordPolicyResultResponse register(@Valid @RequestBody AuthRegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthTokenResponse login(@Valid @RequestBody AuthLoginRequest request) {
        return authService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/refresh")
    public AuthTokenResponse refresh(@Valid @RequestBody AuthRefreshRequest request) {
        return authService.refresh(request.getRefreshToken());
    }

    @PostMapping("/logout")
    public LogoutResponse logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing Bearer token");
        }
        String accessToken = authorizationHeader.substring("Bearer ".length());
        authService.logout(accessToken);
        return new LogoutResponse(true, "Logged out. Session invalidated.");
    }

    @PostMapping("/password/change")
    public PasswordPolicyResultResponse changePassword(@Valid @RequestBody PasswordChangeRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new IllegalStateException("Not authenticated");
        }

        // principal is publicRef as set by JwtAuthFilter
        String publicRef = auth.getPrincipal().toString();
        return authService.changePassword(publicRef, request.getCurrentPassword(), request.getNewPassword());
    }

    @PostMapping("/password/reset/confirm")
    public PasswordPolicyResultResponse confirmReset(@Valid @RequestBody PasswordResetConfirmRequest request) {
        return authService.confirmReset(request.getResetToken(), request.getNewPassword());
    }
}
