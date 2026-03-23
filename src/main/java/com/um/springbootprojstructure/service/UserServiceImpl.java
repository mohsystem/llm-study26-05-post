package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.UserCreateRequest;
import com.um.springbootprojstructure.dto.UserProfileUpdateRequest;
import com.um.springbootprojstructure.dto.UserResponse;
import com.um.springbootprojstructure.dto.UserUpdateRequest;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.mapper.UserMapper;
import com.um.springbootprojstructure.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse create(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User with email already exists: " + request.getEmail());
        }
        User saved = userRepository.save(userMapper.toEntity(request));
        return userMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return userMapper.toResponse(u);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByPublicRef(String publicRef) {
        User u = userRepository.findByPublicRef(publicRef)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for publicRef: " + publicRef));
        return userMapper.toResponse(u);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> list() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest request) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));

        if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(u.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException("User with email already exists: " + request.getEmail());
            }
            u.setEmail(request.getEmail());
        }

        if (request.getFirstName() != null) u.setFirstName(request.getFirstName());
        if (request.getLastName() != null) u.setLastName(request.getLastName());
        if (request.getActive() != null) u.setActive(request.getActive());

        return userMapper.toResponse(userRepository.save(u));
    }

    @Override
    public UserResponse updateProfile(String publicRef, UserProfileUpdateRequest request) {
        User u = userRepository.findByPublicRef(publicRef)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for publicRef: " + publicRef));

        // Allowed fields only
        if (request.getFirstName() != null) u.setFirstName(request.getFirstName());
        if (request.getLastName() != null) u.setLastName(request.getLastName());

        return userMapper.toResponse(userRepository.save(u));
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }
}
