package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.UserRoleUpdateRequest;
import com.um.springbootprojstructure.dto.UserRoleUpdateResponse;

public interface UserRoleAdminService {
    UserRoleUpdateResponse updateRoles(Long userId, UserRoleUpdateRequest request);
}
