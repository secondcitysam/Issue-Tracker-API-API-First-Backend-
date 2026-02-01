package com.samyak.api.entity;

import com.samyak.api.util.AuditableEntity;
import com.samyak.api.util.enums.IssuePriority;
import com.samyak.api.util.enums.IssueStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "issues")
@Getter
@Setter
@NoArgsConstructor
public class Issue extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IssueStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IssuePriority priority;

    @Column(nullable = false)
    private Long createdBy;

    @Column
    private Long assignedTo;
}
