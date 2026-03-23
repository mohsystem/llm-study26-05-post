package com.um.springbootprojstructure.dto;

import jakarta.validation.constraints.NotBlank;

public class UserMergeRequest {

    public enum FieldStrategy {
        SOURCE,
        TARGET,
        NON_EMPTY_PREFER_TARGET
    }

    @NotBlank
    private String sourcePublicRef;

    @NotBlank
    private String targetPublicRef;

    /**
     * Strategy used for firstName/lastName/email.
     * Default: NON_EMPTY_PREFER_TARGET
     */
    private FieldStrategy strategy = FieldStrategy.NON_EMPTY_PREFER_TARGET;

    /**
     * If true, and strategy resolves to SOURCE for email, email will be moved to target.
     * Beware: uniqueness constraints apply.
     */
    private boolean allowEmailMove = false;

    public String getSourcePublicRef() { return sourcePublicRef; }
    public void setSourcePublicRef(String sourcePublicRef) { this.sourcePublicRef = sourcePublicRef; }

    public String getTargetPublicRef() { return targetPublicRef; }
    public void setTargetPublicRef(String targetPublicRef) { this.targetPublicRef = targetPublicRef; }

    public FieldStrategy getStrategy() { return strategy; }
    public void setStrategy(FieldStrategy strategy) { this.strategy = strategy; }

    public boolean isAllowEmailMove() { return allowEmailMove; }
    public void setAllowEmailMove(boolean allowEmailMove) { this.allowEmailMove = allowEmailMove; }
}
