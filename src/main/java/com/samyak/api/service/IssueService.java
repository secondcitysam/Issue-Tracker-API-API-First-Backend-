package com.samyak.api.service;

import com.samyak.api.dto.*;
import com.samyak.api.util.enums.IssuePriority;
import com.samyak.api.util.enums.IssueStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IssueService {

    IssueResponseDTO createIssue(Long userId, IssueCreateDTO dto);

    Page<IssueListDTO> getIssues(
            IssueStatus status,
            IssuePriority priority,
            Pageable pageable
    );

    IssueResponseDTO getIssueById(Long issueId);

    IssueResponseDTO updateIssueStatus(
            Long userId,
            Long issueId,
            IssueStatus newStatus
    );

    IssueResponseDTO assignIssue(
            Long userId,
            Long issueId,
            Long assigneeId
    );

    void archiveIssue(Long userId, Long issueId);
}
