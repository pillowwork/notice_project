package com.rsupport.project.notice.dto;

import com.rsupport.project.notice.entity.NoticeEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class NoticeDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull
        private String title;
        @NotNull
        private String contents;
        @NotNull
        private LocalDateTime startDate;
        @NotNull
        private LocalDateTime endDate;
        @NotNull
        private String author;

    }

    @Getter
    public static class Response {
        private String title;
        private String contents;
        private LocalDateTime createDate;
        private Integer viewCount;
        private String author;

        public Response(NoticeEntity entity) {
            this.title = entity.getTitle();
            this.contents = entity.getContents();
            this.createDate = entity.getCreateDate();
            this.viewCount = entity.getViewCount();
            this.author = entity.getAuthor();
        }

    }
}
