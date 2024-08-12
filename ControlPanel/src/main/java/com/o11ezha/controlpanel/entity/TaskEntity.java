package com.o11ezha.controlpanel.entity;

import com.o11ezha.controlpanel.DTO.enums.TaskPriority;
import com.o11ezha.controlpanel.DTO.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {
    @Id
    @UuidGenerator
    @Column(name = "task_id")
    private UUID taskId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "owner", nullable = false)
    private UserEntity taskOwner;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "performer", nullable = false)
    private UserEntity taskPerformer;

    @Column(name = "task_priority")
    @Enumerated(EnumType.STRING)
    private TaskPriority taskPriority;

    @Column(name = "task_status")
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "task_theme")
    private String taskTheme;

    @Column(name = "task_desc")
    private String taskDesc;
}
