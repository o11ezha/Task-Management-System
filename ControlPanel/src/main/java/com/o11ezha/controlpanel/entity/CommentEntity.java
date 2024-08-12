package com.o11ezha.controlpanel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "comments")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {
    @Id
    @UuidGenerator
    @Column(name = "сomment_id")
    private UUID commentId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "owner", nullable = false)
    private UserEntity commentOwner;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "comment_to_task_id", nullable = false)
    private TaskEntity commentToTaskId;

    @Column(name = "comment_text")
    private String commentText;
}
