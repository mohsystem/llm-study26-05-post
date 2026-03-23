package com.um.springbootprojstructure.config;

import com.um.springbootprojstructure.service.UserAdminService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminBootstrapRunner implements CommandLineRunner {

    private final AdminBootstrapProperties props;
    private final UserAdminService userAdminService;
    private final PromptLogger promptLogger;

    public AdminBootstrapRunner(AdminBootstrapProperties props,
                               UserAdminService userAdminService,
                               PromptLogger promptLogger) {
        this.props = props;
        this.userAdminService = userAdminService;
        this.promptLogger = promptLogger;
    }

    @Override
    public void run(String... args) {
        if (!props.isEnabled()) {
            promptLogger.log("[ADMIN_BOOTSTRAP] disabled");
            return;
        }

        userAdminService.ensureAdminAccount(
                props.getEmail(),
                props.getFirstName(),
                props.getLastName(),
                props.getPassword()
        );

        promptLogger.log("[ADMIN_BOOTSTRAP] ensured admin account for email=" + props.getEmail());
    }
}
