package com.rsupport.project.attachment.dto;

import com.rsupport.project.attachment.entity.AttachmentEntity;
import lombok.Getter;

import java.time.LocalDateTime;

public class AttachmentDto {

    @Getter
    public static class Response {
        private String originalFileName;
        private String storedFileName;
        private String filePath;
        private Long fileSize;
        private String fileType;

        private Boolean isDeleted;
        private LocalDateTime createDate;

        public Response(AttachmentEntity entity) {
            this.originalFileName = entity.getOriginalFileName();
            this.storedFileName = entity.getStoredFileName();
            this.filePath = entity.getFilePath();
            this.fileSize = entity.getFileSize();
            this.fileType = entity.getFileType();
            this.isDeleted = entity.getIsDeleted();
            this.createDate = entity.getCreateDate();
        }
    }
}
