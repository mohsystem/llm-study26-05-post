package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.UserProfileUpdateRequest;
import com.um.springbootprojstructure.dto.UserResponse;
import com.um.springbootprojstructure.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{publicRef}")
    public UserResponse getUserProfile(@PathVariable @NotBlank String publicRef) {
        return userService.getByPublicRef(publicRef);
    }

    @PutMapping("/{publicRef}")
    public UserResponse updateUserProfile(@PathVariable @NotBlank String publicRef,
                                         @Valid @RequestBody UserProfileUpdateRequest request) {
        return userService.updateProfile(publicRef, request);
    }
}
