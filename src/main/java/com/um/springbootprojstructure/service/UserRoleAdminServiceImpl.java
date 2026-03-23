package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.UserRoleUpdateRequest;
import com.um.springbootprojstructure.dto.UserRoleUpdateResponse;
import com.um.springbootprojstructure.entity.Role;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.repository.UserRepository;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserRoleAdminServiceImpl implements UserRoleAdminService {

    private final UserRepository userRepository;

    public UserRoleAdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserRoleUpdateResponse updateRoles(Long userId, UserRoleUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Set<Role> newRoles = request.getRoles().stream()
                .map(r -> r.trim().toUpperCase(Locale.ROOT))
                .map(Role::valueOf)
                .collect(Collectors.toSet());

        if (newRoles.isEmpty()) {
            throw new IllegalArgumentException("At least one role must be provided");
        }

        user.setRoles(newRoles);
        User saved = userRepository.save(user);

        Set<String> roleNames = saved.getRoles().stream().map(Role::name).collect(Collectors.toSet());
        return new UserRoleUpdateResponse(saved.getId(), saved.getPublicRef(), roleNames);
    }
}
