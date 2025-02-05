package com.rsupport.project.notice.dto;

import com.rsupport.project.attachment.dto.AttachmentDto;
import com.rsupport.project.attachment.entity.AttachmentEntity;
import com.rsupport.project.notice.entity.NoticeEntity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NoticeDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        private String title;
        @NotNull
        private String contents;
        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startDate;
        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime endDate;
        @NotNull
        private String author;

        private List<MultipartFile> attachments;
    }

    @Getter
    public static class Response {
        private String title;
        private String contents;
        private LocalDateTime createDate;
        private Integer viewCount;
        private String author;
        private List<AttachmentDto.Response> attachments;

        public Response(NoticeEntity entity) {
            this.title = entity.getTitle();
            this.contents = entity.getContents();
            this.createDate = entity.getCreateDate();
            this.viewCount = entity.getViewCount();
            this.author = entity.getAuthor();
            this.attachments = Optional.ofNullable(entity.getAttachments())
                    .orElseGet(List::of)
                    .stream()
                    .map(attachment -> new AttachmentDto.Response(attachment))
                    .collect(Collectors.toList());
        }
    }
}
