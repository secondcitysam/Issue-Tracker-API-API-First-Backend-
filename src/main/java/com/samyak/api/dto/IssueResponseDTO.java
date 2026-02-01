package com.samyak.api.dto;

import com.samyak.api.util.enums.IssuePriority;
import com.samyak.api.util.enums.IssueStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class IssueResponseDTO {

    private Long id;
    private String title;
    private String description;

    private IssueStatus status;
    private IssuePriority priority;

    private Long createdBy;
    private Long assignedTo;

    private Instant createdAt;
    private Instant updatedAt;
}
