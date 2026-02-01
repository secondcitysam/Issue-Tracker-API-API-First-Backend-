package com.samyak.api.controller.api;

import com.samyak.api.dto.*;
import com.samyak.api.service.IssueService;
import com.samyak.api.util.enums.IssuePriority;
import com.samyak.api.util.enums.IssueStatus;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    // -----------------------------
    // CREATE ISSUE
    // -----------------------------
    @PostMapping
    public ResponseEntity<IssueResponseDTO> createIssue(
            @RequestHeader("X-USER-ID") Long userId,
            @Valid @RequestBody IssueCreateDTO dto
    ) {
        IssueResponseDTO response =
                issueService.createIssue(userId, dto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // -----------------------------
    // LIST ISSUES (FILTER + PAGE)
    // -----------------------------
    @GetMapping
    public ResponseEntity<Page<IssueListDTO>> getIssues(
            @RequestParam(required = false) IssueStatus status,
            @RequestParam(required = false) IssuePriority priority,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<IssueListDTO> result =
                issueService.getIssues(status, priority, pageable);

        return ResponseEntity.ok(result);
    }

    // -----------------------------
    // GET ISSUE BY ID
    // -----------------------------
    @GetMapping("/{id}")
    public ResponseEntity<IssueResponseDTO> getIssueById(
            @PathVariable Long id
    ) {
        IssueResponseDTO response =
                issueService.getIssueById(id);

        return ResponseEntity.ok(response);
    }

    // -----------------------------
    // UPDATE ISSUE STATUS
    // -----------------------------
    @PutMapping("/{id}/status")
    public ResponseEntity<IssueResponseDTO> updateStatus(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody IssueStatusUpdateDTO dto
    ) {
        IssueResponseDTO response =
                issueService.updateIssueStatus(
                        userId,
                        id,
                        dto.getStatus()
                );

        return ResponseEntity.ok(response);
    }

    // -----------------------------
    // ASSIGN ISSUE
    // -----------------------------
    @PutMapping("/{id}/assign")
    public ResponseEntity<IssueResponseDTO> assignIssue(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody IssueAssignmentDTO dto
    ) {
        IssueResponseDTO response =
                issueService.assignIssue(
                        userId,
                        id,
                        dto.getAssigneeId()
                );

        return ResponseEntity.ok(response);
    }

    // -----------------------------
    // ARCHIVE ISSUE
    // -----------------------------
    @PostMapping("/{id}/archive")
    public ResponseEntity<Void> archiveIssue(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long id
    ) {
        issueService.archiveIssue(userId, id);
        return ResponseEntity.noContent().build();
    }
}
