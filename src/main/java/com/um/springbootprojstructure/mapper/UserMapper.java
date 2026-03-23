package com.um.springbootprojstructure.mapper;

import com.um.springbootprojstructure.dto.UserCreateRequest;
import com.um.springbootprojstructure.dto.UserResponse;
import com.um.springbootprojstructure.entity.Role;
import com.um.springbootprojstructure.entity.User;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserCreateRequest req) {
        User u = new User();
        u.setFirstName(req.getFirstName());
        u.setLastName(req.getLastName());
        u.setEmail(req.getEmail());
        u.setActive(true);
        // roles default to USER in @PrePersist if not set
        return u;
    }

    public UserResponse toResponse(User u) {
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setPublicRef(u.getPublicRef());
        r.setFirstName(u.getFirstName());
        r.setLastName(u.getLastName());
        r.setEmail(u.getEmail());
        r.setActive(u.isActive());

        if (u.getRoles() != null) {
            r.setRoles(u.getRoles().stream().map(Role::name).collect(Collectors.toSet()));
        }

        r.setCreatedAt(u.getCreatedAt());
        r.setUpdatedAt(u.getUpdatedAt());
        return r;
    }
}
