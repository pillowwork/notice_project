package com.rsupport.project.notice.entity;

import com.rsupport.project.attachment.entity.AttachmentEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String title;
    @NotNull
    @Column(columnDefinition = "TEXT")
    private String contents;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
    @NotNull
    private String author;

    @Column(updatable = false)
    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.now();
    private int viewCount = 0;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private List<AttachmentEntity> attachments;

}
