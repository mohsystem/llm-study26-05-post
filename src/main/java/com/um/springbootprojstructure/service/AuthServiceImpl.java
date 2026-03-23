package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.AuthTokenResponse;
import com.um.springbootprojstructure.dto.AuthRegisterRequest;
import com.um.springbootprojstructure.dto.PasswordPolicyResultResponse;
import com.um.springbootprojstructure.entity.PasswordResetToken;
import com.um.springbootprojstructure.entity.Session;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.repository.PasswordResetTokenRepository;
import com.um.springbootprojstructure.repository.SessionRepository;
import com.um.springbootprojstructure.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import java.time.Instant;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PasswordPolicyValidator passwordPolicyValidator;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public AuthServiceImpl(UserRepository userRepository,
                           SessionRepository sessionRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           PasswordPolicyValidator passwordPolicyValidator,
                           PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.passwordPolicyValidator = passwordPolicyValidator;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public PasswordPolicyResultResponse register(AuthRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return PasswordPolicyResultResponse.rejected(java.util.List.of("DUPLICATE_EMAIL"));
        }

        User context = new User();
        context.setFirstName(request.getFirstName());
        context.setLastName(request.getLastName());
        context.setEmail(request.getEmail());

        PasswordPolicyResultResponse policy = passwordPolicyValidator.validate(request.getPassword(), context);
        if (!policy.isAccepted()) return policy;

        User u = new User();
        u.setFirstName(request.getFirstName());
        u.setLastName(request.getLastName());
        u.setEmail(request.getEmail());
        u.setActive(true);
        u.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userRepository.save(u);

        return PasswordPolicyResultResponse.accepted();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthTokenResponse login(String email, String password) {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        if (!u.isActive()) {
            throw new IllegalStateException("User is inactive");
        }

        if (!passwordEncoder.matches(password, u.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Create a session row
        Session session = new Session();
        session.setUser(u);
        session = sessionRepository.save(session);

        String access = jwtService.generateAccessToken(u, session.getSessionRef());
        String refresh = jwtService.generateRefreshToken(u, session.getSessionRef());
        return new AuthTokenResponse(access, refresh);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthTokenResponse refresh(String refreshToken) {
        try {
            Jws<Claims> jws = jwtService.parse(refreshToken);
            if (!jwtService.isRefreshToken(jws)) {
                throw new IllegalArgumentException("Provided token is not a refresh token");
            }

            String publicRef = jws.getPayload().getSubject();
            String sessionRef = jws.getPayload().get("sid", String.class);
            if (sessionRef == null || sessionRef.isBlank()) {
                throw new IllegalArgumentException("Refresh token missing session id");
            }

            Session session = sessionRepository.findBySessionRef(sessionRef)
                    .orElseThrow(() -> new IllegalArgumentException("Session is not valid"));

            if (session.isRevoked()) {
                throw new IllegalStateException("Session has been revoked");
            }

            User u = userRepository.findByPublicRef(publicRef)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found for publicRef: " + publicRef));

            if (!u.isActive()) {
                throw new IllegalStateException("User is inactive");
            }

            // Rotate tokens but keep same sessionRef (simple).
            String newAccess = jwtService.generateAccessToken(u, sessionRef);
            String newRefresh = jwtService.generateRefreshToken(u, sessionRef);

            return new AuthTokenResponse(newAccess, newRefresh);
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
    }

    @Override
    public void logout(String accessToken) {
        try {
            Jws<Claims> jws = jwtService.parse(accessToken);
            if (!jwtService.isAccessToken(jws)) {
                throw new IllegalArgumentException("Provided token is not an access token");
            }

            String sessionRef = jws.getPayload().get("sid", String.class);
            if (sessionRef == null || sessionRef.isBlank()) {
                throw new IllegalArgumentException("Access token missing session id");
            }

            Session session = sessionRepository.findBySessionRef(sessionRef)
                    .orElseThrow(() -> new IllegalArgumentException("Session is not valid"));

            if (!session.isRevoked()) {
                session.setRevoked(true);
                session.setRevokedAt(Instant.now());
                sessionRepository.save(session);
            }
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid access token");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PasswordPolicyResultResponse changePassword(String accessTokenPublicRef, String currentPassword, String newPassword) {
        User u = userRepository.findByPublicRef(accessTokenPublicRef)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for publicRef: " + accessTokenPublicRef));

        if (!passwordEncoder.matches(currentPassword, u.getPasswordHash())) {
            return PasswordPolicyResultResponse.rejected(java.util.List.of("INVALID_CURRENT_PASSWORD"));
        }

        PasswordPolicyResultResponse policy = passwordPolicyValidator.validate(newPassword, u);
        if (!policy.isAccepted()) return policy;

        u.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(u);
        return PasswordPolicyResultResponse.accepted();
    }

    @Override
    public PasswordPolicyResultResponse confirmReset(String resetToken, String newPassword) {
        PasswordResetToken prt = passwordResetTokenRepository.findByToken(resetToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reset token"));

        if (prt.isUsed()) {
            return PasswordPolicyResultResponse.rejected(java.util.List.of("RESET_TOKEN_USED"));
        }
        if (prt.getExpiresAt() != null && Instant.now().isAfter(prt.getExpiresAt())) {
            return PasswordPolicyResultResponse.rejected(java.util.List.of("RESET_TOKEN_EXPIRED"));
        }

        User u = prt.getUser();

        PasswordPolicyResultResponse policy = passwordPolicyValidator.validate(newPassword, u);
        if (!policy.isAccepted()) return policy;

        u.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(u);

        prt.setUsed(true);
        prt.setUsedAt(Instant.now());
        passwordResetTokenRepository.save(prt);

        return PasswordPolicyResultResponse.accepted();
    }
}
