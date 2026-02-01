package com.samyak.api.util.enums;

import java.util.Set;

public enum IssueStatus {

    OPEN,
    IN_PROGRESS,
    RESOLVED,
    ARCHIVED;

    public static final Set<IssueStatus> TERMINAL_STATUSES =
            Set.of(RESOLVED, ARCHIVED);
}
