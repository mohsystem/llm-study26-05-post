package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.UserMergeRequest;
import com.um.springbootprojstructure.dto.UserMergeResultResponse;
import com.um.springbootprojstructure.entity.Role;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.repository.UserRepository;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserMergeServiceImpl implements UserMergeService {

    private final UserRepository userRepository;

    public UserMergeServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserMergeResultResponse merge(UserMergeRequest request) {
        if (request.getSourcePublicRef().equals(request.getTargetPublicRef())) {
            throw new IllegalArgumentException("sourcePublicRef and targetPublicRef must be different");
        }

        User source = userRepository.findByPublicRef(request.getSourcePublicRef())
                .orElseThrow(() -> new ResourceNotFoundException("Source user not found: " + request.getSourcePublicRef()));

        User target = userRepository.findByPublicRef(request.getTargetPublicRef())
                .orElseThrow(() -> new ResourceNotFoundException("Target user not found: " + request.getTargetPublicRef()));

        if (!source.isActive()) {
            throw new IllegalArgumentException("Source user is already inactive");
        }
        if (!target.isActive()) {
            throw new IllegalArgumentException("Target user is inactive; cannot merge into inactive account");
        }

        UserMergeResultResponse res = new UserMergeResultResponse();
        res.setSourcePublicRef(source.getPublicRef());
        res.setTargetPublicRef(target.getPublicRef());

        // 1) Merge roles (union)
        if (target.getRoles() == null) target.setRoles(new HashSet<>());
        Set<Role> beforeRoles = new HashSet<>(target.getRoles());

        if (source.getRoles() != null) {
            target.getRoles().addAll(source.getRoles());
        }

        Set<Role> added = new HashSet<>(target.getRoles());
        added.removeAll(beforeRoles);

        res.setRolesAddedToTarget(added.stream().map(Role::name).collect(Collectors.toSet()));

        // 2) Merge selected fields
        boolean firstNameChanged = applyField(request.getStrategy(), source.getFirstName(), target.getFirstName(),
                target::setFirstName);
        boolean lastNameChanged = applyField(request.getStrategy(), source.getLastName(), target.getLastName(),
                target::setLastName);

        res.setFirstNameChanged(firstNameChanged);
        res.setLastNameChanged(lastNameChanged);

        // 3) Email move (optional + guarded)
        boolean emailChanged = false;
        if (request.isAllowEmailMove()) {
            String chosenEmail = chooseValue(request.getStrategy(), source.getEmail(), target.getEmail());
            if (chosenEmail != null && !chosenEmail.equalsIgnoreCase(target.getEmail())) {
                // ensure uniqueness
                boolean exists = userRepository.existsByEmail(chosenEmail);
                boolean sameAsSource = chosenEmail.equalsIgnoreCase(source.getEmail());
                boolean sameAsTarget = chosenEmail.equalsIgnoreCase(target.getEmail());

                // If exists by email and it isn't the source or target email, conflict.
                // Because existsByEmail doesn't tell which record, we handle safe cases:
                if (exists && !(sameAsSource || sameAsTarget)) {
                    throw new DuplicateResourceException("Email already in use: " + chosenEmail);
                }

                // If chosenEmail equals source email, it's safe (moving) only if no third user uses it.
                // existsByEmail could be true because source has it; that's OK.
                target.setEmail(chosenEmail);
                emailChanged = true;
            }
        }
        res.setEmailChanged(emailChanged);

        // 4) Deactivate source account
        source.setActive(false);
        res.setSourceDeactivated(true);

        // Persist both
        userRepository.save(target);
        userRepository.save(source);

        res.setMessage("Merge completed. Source deactivated; target consolidated.");
        return res;
    }

    private interface Setter {
        void set(String v);
    }

    private boolean applyField(UserMergeRequest.FieldStrategy strategy,
                               String sourceVal,
                               String targetVal,
                               Setter targetSetter) {
        String chosen = chooseValue(strategy, sourceVal, targetVal);
        if (chosen == null) {
            return false;
        }
        if (!Objects.equals(chosen, targetVal)) {
            targetSetter.set(chosen);
            return true;
        }
        return false;
    }

    private String chooseValue(UserMergeRequest.FieldStrategy strategy, String sourceVal, String targetVal) {
        return switch (strategy) {
            case SOURCE -> sourceVal;
            case TARGET -> targetVal;
            case NON_EMPTY_PREFER_TARGET -> {
                if (targetVal != null && !targetVal.isBlank()) yield targetVal;
                if (sourceVal != null && !sourceVal.isBlank()) yield sourceVal;
                yield targetVal; // both empty/null => keep target
            }
        };
    }
}
