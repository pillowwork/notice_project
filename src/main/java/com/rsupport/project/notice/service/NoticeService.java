package com.rsupport.project.notice.service;

import com.rsupport.project.attachment.entity.AttachmentEntity;
import com.rsupport.project.attachment.repository.AttachmentRepository;
import com.rsupport.project.notice.dto.NoticeDto;
import com.rsupport.project.notice.entity.NoticeEntity;
import com.rsupport.project.notice.repository.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoticeService {

    private static final String UPLOAD_PATH = "./uploads/";

    private final NoticeRepository noticeRepository;
    private final AttachmentRepository attachmentRepository;

    public NoticeService(
            NoticeRepository noticeRepository,
            AttachmentRepository attachmentRepository
    ) {
        this.noticeRepository = noticeRepository;
        this.attachmentRepository = attachmentRepository;
    }

    public NoticeDto.Response createNotice(NoticeDto.Request request) {
        NoticeEntity entity = NoticeEntity.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .author(request.getAuthor())
                .build();
        noticeRepository.save(entity);

        if (request.getAttachments() != null) {
            List<AttachmentEntity> attachments = request.getAttachments().stream()
                    .map(file -> saveFile(file, entity))
                    .collect(Collectors.toList());
            entity.setAttachments(attachmentRepository.saveAll(attachments));
        }

        return new NoticeDto.Response(entity);
    }

    public Optional<NoticeDto.Response> readNotice(Long id) {
        return noticeRepository.findById(id)
                .map(entity -> {
                    entity.setViewCount(entity.getViewCount() + 1);
                    return new NoticeDto.Response(noticeRepository.save(entity));
                });
    }

    @Transactional
    public NoticeDto.Response updateNotice(Long id, NoticeDto.Request request) {
        return noticeRepository.findById(id)
                .map(entity -> {
                    entity.setTitle(request.getTitle());
                    entity.setContents(request.getContents());
                    entity.setStartDate(request.getStartDate());
                    entity.setEndDate(request.getEndDate());
                    entity.setAuthor(request.getAuthor());

                    if (entity.getAttachments() != null) {
                        attachmentRepository.deleteAll(entity.getAttachments());
                        entity.getAttachments().clear();
                    }

                    if (request.getAttachments() != null) {
                        List<AttachmentEntity> attachments = request.getAttachments().stream()
                                .map(file -> saveFile(file, entity))
                                .collect(Collectors.toList());
                        entity.setAttachments(attachmentRepository.saveAll(attachments));
                    }

                    noticeRepository.save(entity);
                    return new NoticeDto.Response(entity);
                })
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));
    }

    public void deleteNotice(Long id) {
        noticeRepository.findById(id).ifPresent(entity -> {
            if(entity.getAttachments() != null) {
                attachmentRepository.deleteAll(entity.getAttachments());
            }
            noticeRepository.delete(entity);
        });
    }

    private AttachmentEntity saveFile(MultipartFile file, NoticeEntity notice) {
        try {
            Path uploadPath = Path.of(UPLOAD_PATH);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String storedFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(storedFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return AttachmentEntity.builder()
                    .originalFileName(file.getOriginalFilename())
                    .storedFileName(storedFileName)
                    .filePath(filePath.toString())
                    .fileSize(file.getSize())
                    .fileType(file.getContentType())
                    .notice(notice)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }

}
