package com.um.springbootprojstructure.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.um.springbootprojstructure.dto.UserXmlImportResponse;
import com.um.springbootprojstructure.dto.legacy.LegacyUserXml;
import com.um.springbootprojstructure.dto.legacy.LegacyUsersXml;
import com.um.springbootprojstructure.entity.Role;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.repository.UserRepository;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class UserXmlImportServiceImpl implements UserXmlImportService {

    private static final Pattern BASIC_EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private final XmlMapper xmlMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final SecureRandom secureRandom = new SecureRandom();

    public UserXmlImportServiceImpl(XmlMapper xmlMapper,
                                    UserRepository userRepository,
                                    PasswordEncoder passwordEncoder) {
        this.xmlMapper = xmlMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserXmlImportResponse importUsers(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("XML file is required");
        }
        if (file.getOriginalFilename() != null && !file.getOriginalFilename().toLowerCase(Locale.ROOT).endsWith(".xml")) {
            // not strictly necessary, but helps
        }

        LegacyUsersXml legacy;
        try (InputStream in = file.getInputStream()) {
            legacy = xmlMapper.readValue(in, LegacyUsersXml.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse XML: " + e.getMessage());
        }

        UserXmlImportResponse resp = new UserXmlImportResponse();
        int total = legacy.getUsers() == null ? 0 : legacy.getUsers().size();
        resp.setTotal(total);

        if (legacy.getUsers() == null) return resp;

        for (int i = 0; i < legacy.getUsers().size(); i++) {
            LegacyUserXml lu = legacy.getUsers().get(i);

            String email = lu == null ? null : safeTrim(lu.getEmail());
            try {
                if (lu == null) {
                    reject(resp, i, null, "Null user record");
                    continue;
                }
                if (isBlank(lu.getFirstName())) {
                    reject(resp, i, email, "Missing firstName");
                    continue;
                }
                if (isBlank(lu.getLastName())) {
                    reject(resp, i, email, "Missing lastName");
                    continue;
                }
                if (isBlank(email) || !BASIC_EMAIL.matcher(email).matches()) {
                    reject(resp, i, email, "Missing/invalid email");
                    continue;
                }

                if (userRepository.existsByEmail(email)) {
                    skip(resp, i, email, "Email already exists");
                    continue;
                }

                User u = new User();
                u.setFirstName(lu.getFirstName().trim());
                u.setLastName(lu.getLastName().trim());
                u.setEmail(email);
                u.setActive(lu.getActive() == null || lu.getActive());

                Set<Role> roles = parseRoles(lu);
                u.setRoles(roles);

                String tempPassword = generateTempPassword(20);
                u.setPasswordHash(passwordEncoder.encode(tempPassword));

                userRepository.save(u);

                resp.setImported(resp.getImported() + 1);
                resp.getResults().add(new UserXmlImportResponse.RecordResult(i, email, "IMPORTED", null));
            } catch (Exception ex) {
                reject(resp, i, email, ex.getMessage());
            }
        }

        return resp;
    }

    private void skip(UserXmlImportResponse resp, int idx, String email, String reason) {
        resp.setSkipped(resp.getSkipped() + 1);
        resp.getResults().add(new UserXmlImportResponse.RecordResult(idx, email, "SKIPPED", reason));
    }

    private void reject(UserXmlImportResponse resp, int idx, String email, String reason) {
        resp.setRejected(resp.getRejected() + 1);
        resp.getResults().add(new UserXmlImportResponse.RecordResult(idx, email, "REJECTED", reason));
    }

    private Set<Role> parseRoles(LegacyUserXml lu) {
        Set<Role> roles = new HashSet<>();
        if (lu.getRoles() == null || lu.getRoles().isEmpty()) {
            roles.add(Role.USER);
            return roles;
        }
        for (String r : lu.getRoles()) {
            if (r == null) continue;
            String norm = r.trim().toUpperCase(Locale.ROOT);
            if (norm.isBlank()) continue;
            try {
                roles.add(Role.valueOf(norm));
            } catch (IllegalArgumentException ex) {
                // Unknown legacy role -> ignore and default to USER if empty
            }
        }
        if (roles.isEmpty()) roles.add(Role.USER);
        return roles;
    }

    private String generateTempPassword(int len) {
        final String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$_-";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isBlank();
    }

    private String safeTrim(String s) {
        return s == null ? null : s.trim();
    }
}
