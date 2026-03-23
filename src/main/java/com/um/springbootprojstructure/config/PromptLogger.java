package com.um.springbootprojstructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PromptLogger {
    private static final Logger PROMPT_LOG = LoggerFactory.getLogger("USER_PROMPT_LOG");

    public void log(String message) {
        PROMPT_LOG.info(message);
    }
}
