package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.AuthTokenResponse;
import com.um.springbootprojstructure.dto.AuthRegisterRequest;
import com.um.springbootprojstructure.dto.PasswordPolicyResultResponse;

public interface AuthService {
    PasswordPolicyResultResponse register(AuthRegisterRequest request);
    AuthTokenResponse login(String email, String password);
    AuthTokenResponse refresh(String refreshToken);
    void logout(String accessToken);
    PasswordPolicyResultResponse changePassword(String accessTokenPublicRef, String currentPassword, String newPassword);
    PasswordPolicyResultResponse confirmReset(String resetToken, String newPassword);
}
