package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.entity.Role;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.repository.UserRepository;
import java.util.HashSet;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserAdminServiceImpl implements UserAdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAdminServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void ensureAdminAccount(String email, String firstName, String lastName, String password) {
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User u = new User();
            u.setEmail(email);
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setActive(true);
            u.setRoles(new HashSet<>());
            return u;
        });

        if (user.getRoles() == null) user.setRoles(new HashSet<>());
        user.getRoles().add(Role.ADMIN);
        user.getRoles().add(Role.USER);

        // if missing passwordHash, set it
        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(password));
        }

        userRepository.save(user);
    }
}
