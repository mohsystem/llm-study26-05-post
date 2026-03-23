package com.um.springbootprojstructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        PromptLogProperties.class,
        AdminBootstrapProperties.class,
        JwtProperties.class,
        LdapValidationProperties.class
})
public class PropertiesConfig {}
