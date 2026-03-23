package com.um.springbootprojstructure.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class PromptLogStartupRunner implements ApplicationRunner {

    private final PromptLogProperties props;
    private final PromptLogger promptLogger;

    public PromptLogStartupRunner(PromptLogProperties props, PromptLogger promptLogger) {
        this.props = props;
        this.promptLogger = promptLogger;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (props.getInitial() != null) {
            for (String line : props.getInitial()) {
                promptLogger.log("[INITIAL_USER_PROMPT] " + line);
            }
        }
    }
}
