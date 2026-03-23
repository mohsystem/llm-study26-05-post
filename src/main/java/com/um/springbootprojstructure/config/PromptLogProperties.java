package com.um.springbootprojstructure.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.promptlog")
public class PromptLogProperties {
    private List<String> initial = new ArrayList<>();

    public List<String> getInitial() {
        return initial;
    }

    public void setInitial(List<String> initial) {
        this.initial = initial;
    }
}
