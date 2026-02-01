package com.samyak.api.service.impl;

import com.samyak.api.dto.*;
import com.samyak.api.entity.Issue;
import com.samyak.api.entity.User;
import com.samyak.api.exception.AccessDeniedException;
import com.samyak.api.exception.InvalidStateException;
import com.samyak.api.exception.ResourceNotFoundException;
import com.samyak.api.repository.IssueRepository;
import com.samyak.api.repository.UserRepository;
import com.samyak.api.service.IssueService;
import com.samyak.api.util.enums.IssuePriority;
import com.samyak.api.util.enums.IssueStatus;
import com.samyak.api.util.enums.UserStatus;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public IssueServiceImpl(
            IssueRepository issueRepository,
            UserRepository userRepository,
            ModelMapper modelMapper
    ) {
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    // -----------------------------
    // CREATE ISSUE
    // -----------------------------
    @Override
    public IssueResponseDTO createIssue(Long userId, IssueCreateDTO dto) {

        User creator = userRepository
                .findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found or inactive")
                );

        Issue issue = new Issue();
        issue.setTitle(dto.getTitle());
        issue.setDescription(dto.getDescription());
        issue.setPriority(dto.getPriority());
        issue.setStatus(IssueStatus.OPEN);
        issue.setCreatedBy(creator.getId());

        Issue saved = issueRepository.save(issue);
        return modelMapper.map(saved, IssueResponseDTO.class);
    }

    // -----------------------------
    // LIST ISSUES (FILTER + PAGE)
    // -----------------------------
    @Override
    @Transactional(readOnly = true)
    public Page<IssueListDTO> getIssues(
            IssueStatus status,
            IssuePriority priority,
            Pageable pageable
    ) {
        return issueRepository.findFilteredIssues(status, priority, pageable);
    }

    // -----------------------------
    // GET ISSUE BY ID
    // -----------------------------
    @Override
    @Transactional(readOnly = true)
    public IssueResponseDTO getIssueById(Long issueId) {

        Issue issue = issueRepository
                .findByIdAndStatusNot(issueId, IssueStatus.ARCHIVED)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Issue not found")
                );

        return modelMapper.map(issue, IssueResponseDTO.class);
    }

    // -----------------------------
    // UPDATE ISSUE STATUS
    // -----------------------------
    @Override
    public IssueResponseDTO updateIssueStatus(
            Long userId,
            Long issueId,
            IssueStatus newStatus
    ) {
        Issue issue = issueRepository
                .findByIdAndStatusNot(issueId, IssueStatus.ARCHIVED)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Issue not found")
                );

        if (issue.getAssignedTo() == null ||
                !issue.getAssignedTo().equals(userId)) {
            throw new AccessDeniedException("Only assigned user can update status");
        }

        validateStatusTransition(issue.getStatus(), newStatus);

        issue.setStatus(newStatus);
        Issue updated = issueRepository.save(issue);

        return modelMapper.map(updated, IssueResponseDTO.class);
    }

    // -----------------------------
    // ASSIGN ISSUE
    // -----------------------------
    @Override
    public IssueResponseDTO assignIssue(
            Long userId,
            Long issueId,
            Long assigneeId
    ) {
        Issue issue = issueRepository
                .findByIdAndStatusNot(issueId, IssueStatus.ARCHIVED)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Issue not found")
                );

        if (!issue.getCreatedBy().equals(userId)) {
            throw new AccessDeniedException("Only creator can assign issue");
        }

        User assignee = userRepository
                .findByIdAndStatus(assigneeId, UserStatus.ACTIVE)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Assignee not found or inactive")
                );

        issue.setAssignedTo(assignee.getId());
        Issue updated = issueRepository.save(issue);

        return modelMapper.map(updated, IssueResponseDTO.class);
    }

    // -----------------------------
    // ARCHIVE ISSUE
    // -----------------------------
    @Override
    public void archiveIssue(Long userId, Long issueId) {

        Issue issue = issueRepository
                .findByIdAndStatusNot(issueId, IssueStatus.ARCHIVED)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Issue not found")
                );

        if (!issue.getCreatedBy().equals(userId)) {
            throw new AccessDeniedException("Only creator can archive issue");
        }

        issue.setStatus(IssueStatus.ARCHIVED);
        issueRepository.save(issue);
    }

    // -----------------------------
    // STATUS TRANSITION VALIDATION
    // -----------------------------
    private void validateStatusTransition(
            IssueStatus current,
            IssueStatus next
    ) {
        if (current == IssueStatus.OPEN &&
                next == IssueStatus.IN_PROGRESS) {
            return;
        }

        if (current == IssueStatus.IN_PROGRESS &&
                next == IssueStatus.RESOLVED) {
            return;
        }

        throw new InvalidStateException(
                "Invalid status transition: " + current + " → " + next
        );
    }
}
