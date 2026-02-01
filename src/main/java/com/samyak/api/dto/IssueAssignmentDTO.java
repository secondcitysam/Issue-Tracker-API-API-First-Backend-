package com.samyak.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueAssignmentDTO {

    @NotNull(message = "Assignee userId is required")
    private Long assigneeId;
}
