package com.samyak.api.dto;

import com.samyak.api.util.enums.IssueStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueStatusUpdateDTO {

    @NotNull(message = "Status is required")
    private IssueStatus status;
}
