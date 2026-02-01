package com.samyak.api.repository;

import com.samyak.api.entity.Issue;
import com.samyak.api.util.enums.IssuePriority;
import com.samyak.api.util.enums.IssueStatus;
import com.samyak.api.dto.IssueListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    // -----------------------------
    // BASIC SAFETY QUERIES
    // -----------------------------

    Optional<Issue> findByIdAndStatusNot(Long id, IssueStatus status);

    Page<Issue> findByAssignedToAndStatusNot(
            Long assignedTo,
            IssueStatus status,
            Pageable pageable
    );

    // -----------------------------
    // JPQL DTO PROJECTION (CORE)
    // -----------------------------

    @Query("""
        SELECT new com.samyak.api.dto.IssueListDTO(
            i.id,
            i.title,
            i.status,
            i.priority,
            i.createdAt
        )
        FROM Issue i
        WHERE (:status IS NULL OR i.status = :status)
          AND (:priority IS NULL OR i.priority = :priority)
          AND i.status <> 'ARCHIVED'
    """)
    Page<IssueListDTO> findFilteredIssues(
            @Param("status") IssueStatus status,
            @Param("priority") IssuePriority priority,
            Pageable pageable
    );
}
