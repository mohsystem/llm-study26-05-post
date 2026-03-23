package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.PasswordRulesRequest;
import com.um.springbootprojstructure.dto.PasswordRulesResponse;
import com.um.springbootprojstructure.entity.PasswordRules;
import com.um.springbootprojstructure.mapper.PasswordRulesMapper;
import com.um.springbootprojstructure.repository.PasswordRulesRepository;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PasswordRulesServiceImpl implements PasswordRulesService {

    private static final long SINGLETON_ID = 1L;

    private final PasswordRulesRepository repo;
    private final PasswordRulesMapper mapper;

    public PasswordRulesServiceImpl(PasswordRulesRepository repo, PasswordRulesMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public PasswordRulesResponse getActiveRules() {
        PasswordRules rules = repo.findById(SINGLETON_ID).orElseGet(() -> repo.save(defaultRules()));
        return mapper.toResponse(rules);
    }

    @Override
    public PasswordRulesResponse updateRules(PasswordRulesRequest request) {
        validateConsistency(request);

        PasswordRules rules = repo.findById(SINGLETON_ID).orElseGet(this::defaultRules);
        rules.setId(SINGLETON_ID);

        mapper.applyRequest(rules, request);
        rules.setUpdatedAt(Instant.now());

        PasswordRules saved = repo.save(rules);
        return mapper.toResponse(saved);
    }

    private void validateConsistency(PasswordRulesRequest req) {
        if (req.isRequireUppercase() && req.getMinUppercase() <= 0) {
            throw new IllegalArgumentException("minUppercase must be > 0 when requireUppercase is true");
        }
        if (!req.isRequireUppercase() && req.getMinUppercase() != 0) {
            throw new IllegalArgumentException("minUppercase must be 0 when requireUppercase is false");
        }

        if (req.isRequireLowercase() && req.getMinLowercase() <= 0) {
            throw new IllegalArgumentException("minLowercase must be > 0 when requireLowercase is true");
        }
        if (!req.isRequireLowercase() && req.getMinLowercase() != 0) {
            throw new IllegalArgumentException("minLowercase must be 0 when requireLowercase is false");
        }

        if (req.isRequireDigit() && req.getMinDigits() <= 0) {
            throw new IllegalArgumentException("minDigits must be > 0 when requireDigit is true");
        }
        if (!req.isRequireDigit() && req.getMinDigits() != 0) {
            throw new IllegalArgumentException("minDigits must be 0 when requireDigit is false");
        }

        if (req.isRequireSpecial() && req.getMinSpecial() <= 0) {
            throw new IllegalArgumentException("minSpecial must be > 0 when requireSpecial is true");
        }
        if (!req.isRequireSpecial() && req.getMinSpecial() != 0) {
            throw new IllegalArgumentException("minSpecial must be 0 when requireSpecial is false");
        }

        int mins = req.getMinUppercase() + req.getMinLowercase() + req.getMinDigits() + req.getMinSpecial();
        if (req.getMinLength() < mins) {
            throw new IllegalArgumentException("minLength is too small for the required minimum character counts");
        }
    }

    private PasswordRules defaultRules() {
        PasswordRules r = new PasswordRules();
        r.setId(SINGLETON_ID);
        r.setUpdatedAt(Instant.now());
        return r;
    }
}
