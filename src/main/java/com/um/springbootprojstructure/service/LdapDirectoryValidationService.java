package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.config.LdapValidationProperties;
import com.um.springbootprojstructure.dto.DirectoryValidationRequest;
import com.um.springbootprojstructure.dto.DirectoryValidationResponse;
import com.um.springbootprojstructure.dto.DirectoryValidationResponse.Status;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.directory.Attributes;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LdapDirectoryValidationService implements DirectoryValidationService {

    private final LdapValidationProperties props;
    private final LdapTemplate ldapTemplate;

    public LdapDirectoryValidationService(LdapValidationProperties props, LdapTemplate ldapTemplate) {
        this.props = props;
        this.ldapTemplate = ldapTemplate;
    }

    @Override
    public DirectoryValidationResponse validate(DirectoryValidationRequest request) {
        if (!hasAnyIdentityAttribute(request)) {
            throw new IllegalArgumentException("At least one identity attribute must be provided");
        }

        if (props.isMock()) {
            return mockValidate(request);
        }

        try {
            AndFilter filter = new AndFilter();
            filter.and(new EqualsFilter("objectClass", props.getUserObjectClass()));

            // Adjust these attribute names to your directory schema:
            if (StringUtils.hasText(request.getEmail())) {
                filter.and(new EqualsFilter("mail", request.getEmail().trim()));
            }
            if (StringUtils.hasText(request.getUsername())) {
                // could be uid or sAMAccountName depending on directory
                filter.and(new EqualsFilter("uid", request.getUsername().trim()));
            }
            if (StringUtils.hasText(request.getFirstName())) {
                filter.and(new LikeFilter("givenName", request.getFirstName().trim()));
            }
            if (StringUtils.hasText(request.getLastName())) {
                filter.and(new LikeFilter("sn", request.getLastName().trim()));
            }

            List<DirectoryHit> hits = ldapTemplate.search(
                    props.getUserSearchBase(),
                    filter.encode(),
                    (AttributesMapper<DirectoryHit>) attrs -> mapAttributes(attrs)
            );

            if (hits.isEmpty()) {
                return new DirectoryValidationResponse(Status.NOT_FOUND, null, null, "No directory entry matched");
            }
            if (hits.size() > 1) {
                // avoid returning all DNs/attributes unless you want to
                return new DirectoryValidationResponse(Status.AMBIGUOUS, null, Map.of("count", hits.size()),
                        "Multiple directory entries matched");
            }

            DirectoryHit hit = hits.get(0);
            return new DirectoryValidationResponse(Status.MATCHED, hit.dn, hit.attributes, "Matched exactly one entry");
        } catch (Exception e) {
            return new DirectoryValidationResponse(Status.ERROR, null, null, "LDAP error: " + e.getMessage());
        }
    }

    private boolean hasAnyIdentityAttribute(DirectoryValidationRequest req) {
        return StringUtils.hasText(req.getEmail())
                || StringUtils.hasText(req.getUsername())
                || StringUtils.hasText(req.getFirstName())
                || StringUtils.hasText(req.getLastName());
    }

    /**
     * Note: AttributesMapper does not provide DN by default. If DN is needed,
     * we can switch to ContextMapper in a follow-up.
     * For now, dn is returned as null in real LDAP mode unless you want ContextMapper.
     */
    private DirectoryHit mapAttributes(Attributes attrs) throws Exception {
        Map<String, Object> map = new HashMap<>();
        // Choose what you want to return
        putIfPresent(attrs, map, "mail");
        putIfPresent(attrs, map, "uid");
        putIfPresent(attrs, map, "givenName");
        putIfPresent(attrs, map, "sn");
        putIfPresent(attrs, map, "cn");

        return new DirectoryHit(null, map);
    }

    private void putIfPresent(Attributes attrs, Map<String, Object> out, String name) throws Exception {
        var a = attrs.get(name);
        if (a != null && a.get() != null) {
            out.put(name, a.get());
        }
    }

    private DirectoryValidationResponse mockValidate(DirectoryValidationRequest request) {
        // Simple deterministic mock for local testing
        if (StringUtils.hasText(request.getEmail()) && request.getEmail().toLowerCase().contains("nomatch")) {
            return new DirectoryValidationResponse(Status.NOT_FOUND, null, null, "Mock: no match");
        }
        if (StringUtils.hasText(request.getEmail()) && request.getEmail().toLowerCase().contains("multi")) {
            return new DirectoryValidationResponse(Status.AMBIGUOUS, null, Map.of("count", 2), "Mock: multiple matches");
        }

        Map<String, Object> attrs = new HashMap<>();
        attrs.put("mail", request.getEmail() != null ? request.getEmail().trim() : "john.doe@example.com");
        attrs.put("uid", request.getUsername() != null ? request.getUsername().trim() : "jdoe");
        attrs.put("givenName", request.getFirstName() != null ? request.getFirstName().trim() : "John");
        attrs.put("sn", request.getLastName() != null ? request.getLastName().trim() : "Doe");
        attrs.put("cn", "John Doe");

        return new DirectoryValidationResponse(Status.MATCHED, "uid=jdoe,ou=people,dc=example,dc=com", attrs, "Mock: matched");
    }

    private record DirectoryHit(String dn, Map<String, Object> attributes) {}
}
