package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.UserCreateRequest;
import com.um.springbootprojstructure.dto.UserProfileUpdateRequest;
import com.um.springbootprojstructure.dto.UserResponse;
import com.um.springbootprojstructure.dto.UserUpdateRequest;
import java.util.List;

public interface UserService {
    UserResponse create(UserCreateRequest request);

    UserResponse getById(Long id);
    UserResponse getByPublicRef(String publicRef);

    List<UserResponse> list();

    UserResponse update(Long id, UserUpdateRequest request);

    // NEW: profile update by publicRef (limited fields)
    UserResponse updateProfile(String publicRef, UserProfileUpdateRequest request);

    void delete(Long id);
}
