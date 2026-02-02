package com.samyak.api.service.impl;

import com.samyak.api.entity.Issue;
import com.samyak.api.exception.AccessDeniedException;
import com.samyak.api.exception.InvalidStateException;
import com.samyak.api.exception.ResourceNotFoundException;
import com.samyak.api.repository.IssueRepository;
import com.samyak.api.repository.UserRepository;
import com.samyak.api.util.enums.IssueStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IssueServiceImplTest {

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private IssueServiceImpl issueService;


    // -----------------------------
    // TEST 1: INVALID STATUS TRANSITION
    // OPEN -> RESOLVED (not allowed)
    // -----------------------------
    @Test
    void updateIssueStatus_shouldThrowInvalidState_whenTransitionIsInvalid() {

        Issue issue = new Issue();
        issue.setId(1L);
        issue.setStatus(IssueStatus.OPEN);
        issue.setAssignedTo(10L);

        when(issueRepository.findByIdAndStatusNot(1L, IssueStatus.ARCHIVED))
                .thenReturn(Optional.of(issue));

        assertThrows(
                InvalidStateException.class,
                () -> issueService.updateIssueStatus(
                        10L,
                        1L,
                        IssueStatus.RESOLVED
                )
        );
    }


    // -----------------------------
    // TEST 2: ONLY ASSIGNED USER CAN UPDATE STATUS
    // -----------------------------
    @Test
    void updateIssueStatus_shouldThrowAccessDenied_whenUserIsNotAssignee() {

        Issue issue = new Issue();
        issue.setId(1L);
        issue.setStatus(IssueStatus.OPEN);
        issue.setAssignedTo(10L);

        when(issueRepository.findByIdAndStatusNot(1L, IssueStatus.ARCHIVED))
                .thenReturn(Optional.of(issue));

        assertThrows(
                AccessDeniedException.class,
                () -> issueService.updateIssueStatus(
                        99L,
                        1L,
                        IssueStatus.IN_PROGRESS
                )
        );
    }


    // -----------------------------
    // TEST 3: ARCHIVED ISSUE IS NOT FOUND
    // -----------------------------
    @Test
    void updateIssueStatus_shouldThrowNotFound_whenIssueIsArchived() {

        when(issueRepository.findByIdAndStatusNot(1L, IssueStatus.ARCHIVED))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> issueService.updateIssueStatus(
                        10L,
                        1L,
                        IssueStatus.IN_PROGRESS
                )
        );
    }

    // -----------------------------
    // TEST 4: ONLY CREATOR CAN ASSIGN ISSUE
    // -----------------------------
    @Test
    void assignIssue_shouldThrowAccessDenied_whenUserIsNotCreator() {

        Issue issue = new Issue();
        issue.setId(1L);
        issue.setCreatedBy(10L); // creator is user 10

        when(issueRepository.findByIdAndStatusNot(1L, IssueStatus.ARCHIVED))
                .thenReturn(Optional.of(issue));

        assertThrows(
                AccessDeniedException.class,
                () -> issueService.assignIssue(
                        99L,   // NOT creator
                        1L,
                        20L
                )
        );
    }

    // -----------------------------
    // TEST 5: ONLY CREATOR CAN ARCHIVE ISSUE
    // -----------------------------
    @Test
    void archiveIssue_shouldThrowAccessDenied_whenUserIsNotCreator() {

        Issue issue = new Issue();
        issue.setId(1L);
        issue.setCreatedBy(10L); // creator is user 10

        when(issueRepository.findByIdAndStatusNot(1L, IssueStatus.ARCHIVED))
                .thenReturn(Optional.of(issue));

        assertThrows(
                AccessDeniedException.class,
                () -> issueService.archiveIssue(
                        99L,   // NOT creator
                        1L
                )
        );
    }


}
