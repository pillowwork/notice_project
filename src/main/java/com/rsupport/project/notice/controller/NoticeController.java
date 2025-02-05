package com.rsupport.project.notice.controller;

import com.rsupport.project.notice.dto.NoticeDto;
import com.rsupport.project.notice.service.NoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(
            NoticeService noticeService
    ) {
        this.noticeService = noticeService;
    }

    @PostMapping
    public ResponseEntity<NoticeDto.Response> createNotice(
            @ModelAttribute NoticeDto.Request request
    ) {
        return ResponseEntity.ok(noticeService.createNotice(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeDto.Response> readNotice(
            @PathVariable Long id
    ) {
        return noticeService.readNotice(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoticeDto.Response> updateNotice(
            @PathVariable Long id,
            @ModelAttribute NoticeDto.Request request
    ) {
        return ResponseEntity.ok(noticeService.updateNotice(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(
            @PathVariable Long id
    ) {
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }

}
