package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.UserMergeRequest;
import com.um.springbootprojstructure.dto.UserMergeResultResponse;

public interface UserMergeService {
    UserMergeResultResponse merge(UserMergeRequest request);
}
