package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.DirectoryValidationRequest;
import com.um.springbootprojstructure.dto.DirectoryValidationResponse;

public interface DirectoryValidationService {
    DirectoryValidationResponse validate(DirectoryValidationRequest request);
}
