package com.samyak.api.dto;

import com.samyak.api.util.enums.IssuePriority;
import com.samyak.api.util.enums.IssueStatus;
import lombok.Getter;

import java.time.Instant;

@Getter
public class IssueListDTO {

    private final Long id;
    private final String title;
    private final IssueStatus status;
    private final IssuePriority priority;
    private final Instant createdAt;

    public IssueListDTO(
            Long id,
            String title,
            IssueStatus status,
            IssuePriority priority,
            Instant createdAt
    ) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
    }
}
