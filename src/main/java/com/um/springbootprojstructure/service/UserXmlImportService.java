package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.UserXmlImportResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserXmlImportService {
    UserXmlImportResponse importUsers(MultipartFile file);
}
