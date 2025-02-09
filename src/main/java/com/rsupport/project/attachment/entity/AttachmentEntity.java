package com.rsupport.project.attachment.entity;

import com.rsupport.project.notice.entity.NoticeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "attachment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;
    private String storedFileName;
    private String filePath;
    private Long fileSize;
    private String fileType;

    @Column(updatable = false)
    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "notice_id", nullable = false)
    private NoticeEntity notice;

}
