package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.DirectoryValidationRequest;
import com.um.springbootprojstructure.dto.DirectoryValidationResponse;
import com.um.springbootprojstructure.dto.UserMergeRequest;
import com.um.springbootprojstructure.dto.UserMergeResultResponse;
import com.um.springbootprojstructure.dto.UserRoleUpdateRequest;
import com.um.springbootprojstructure.dto.UserRoleUpdateResponse;
import com.um.springbootprojstructure.dto.UserXmlImportResponse;
import com.um.springbootprojstructure.service.DirectoryValidationService;
import com.um.springbootprojstructure.service.UserMergeService;
import com.um.springbootprojstructure.service.UserRoleAdminService;
import com.um.springbootprojstructure.service.UserXmlImportService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserMergeService userMergeService;
    private final UserRoleAdminService userRoleAdminService;
    private final UserXmlImportService userXmlImportService;
    private final DirectoryValidationService directoryValidationService;

    public AdminUserController(UserMergeService userMergeService,
                               UserRoleAdminService userRoleAdminService,
                               UserXmlImportService userXmlImportService,
                               DirectoryValidationService directoryValidationService) {
        this.userMergeService = userMergeService;
        this.userRoleAdminService = userRoleAdminService;
        this.userXmlImportService = userXmlImportService;
        this.directoryValidationService = directoryValidationService;
    }

    @PostMapping("/merge")
    public UserMergeResultResponse mergeUsers(@Valid @RequestBody UserMergeRequest request) {
        return userMergeService.merge(request);
    }

    @PutMapping("/{id}/role")
    public UserRoleUpdateResponse updateUserRole(@PathVariable Long id,
                                                 @Valid @RequestBody UserRoleUpdateRequest request) {
        return userRoleAdminService.updateRoles(id, request);
    }

    @PostMapping(value = "/import-xml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserXmlImportResponse importXml(@RequestPart("file") MultipartFile file) {
        return userXmlImportService.importUsers(file);
    }

    @PostMapping("/validate-directory")
    public DirectoryValidationResponse validateDirectory(@Valid @RequestBody DirectoryValidationRequest request) {
        return directoryValidationService.validate(request);
    }
}
