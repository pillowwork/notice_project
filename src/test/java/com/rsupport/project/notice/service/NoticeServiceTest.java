package com.rsupport.project.notice.service;

import com.rsupport.project.attachment.repository.AttachmentRepository;
import com.rsupport.project.notice.dto.NoticeDto;
import com.rsupport.project.notice.entity.NoticeEntity;
import com.rsupport.project.notice.repository.NoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private AttachmentRepository attachmentRepository;

    @InjectMocks
    private NoticeService noticeService;

    private NoticeEntity noticeEntity;
    private NoticeDto.Request createRequestDto;
    private NoticeDto.Request updateRequestDto;

    @BeforeEach
    void setUp() {
        noticeEntity = NoticeEntity.builder()
                .title("테스트 공지사항")
                .contents("테스트 공지 내용입니다.")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .author("테스트 작성자")
                .viewCount(0)
                .build();
        noticeRepository.saveAndFlush(noticeEntity);

        createRequestDto = new NoticeDto.Request();
        createRequestDto.setTitle("테스트 공지사항");
        createRequestDto.setContents("테스트 공지 내용입니다.");
        createRequestDto.setAuthor("테스트 작성자");

        updateRequestDto = new NoticeDto.Request();
        updateRequestDto.setTitle("수정된 공지 제목");
        updateRequestDto.setContents("수정된 공지 내용 압니다.");
        updateRequestDto.setAuthor("수정된 작성자");
    }

    @Test // 공지사항 등록 테스트
    void testCreateNotice() {
        when(noticeRepository.save(any(NoticeEntity.class))).thenReturn(noticeEntity);

        NoticeDto.Response response = noticeService.createNotice(createRequestDto);

        assertNotNull(response);
        assertEquals("테스트 공지사항", response.getTitle());
        assertEquals("테스트 공지 내용입니다.", response.getContents());
        assertEquals("테스트 작성자", response.getAuthor());
        verify(noticeRepository, times(1)).save(any(NoticeEntity.class));
    }

    @Test // 공지사항 조회 테스트
    void testReadNotice() {
        when(noticeRepository.findById(1L)).thenReturn(Optional.of(noticeEntity));
        when(noticeRepository.save(any(NoticeEntity.class))).thenReturn(noticeEntity);

        Optional<NoticeDto.Response> response = noticeService.readNotice(1L);

        assertTrue(response.isPresent());
        assertEquals("테스트 공지사항", response.get().getTitle());
        assertEquals("테스트 공지 내용입니다.", response.get().getContents());
        assertEquals("테스트 작성자", response.get().getAuthor());
        verify(noticeRepository, times(1)).findById(1L);
    }

    @Test // 공지사항 수정 테스트
    void updateNotice() {
        when(noticeRepository.findById(1L)).thenReturn(Optional.of(noticeEntity));
        when(noticeRepository.save(any(NoticeEntity.class))).thenReturn(noticeEntity);

        NoticeDto.Response response = noticeService.updateNotice(1L, updateRequestDto);

        assertNotNull(response);
        assertEquals("수정된 공지 제목", response.getTitle());
        assertEquals("수정된 공지 내용 압니다.", response.getContents());
        assertEquals("수정된 작성자", response.getAuthor());
        verify(noticeRepository, times(1)).findById(1L);
        verify(noticeRepository, times(1)).save(any(NoticeEntity.class));
    }

    @Test // 공지사항 삭제 테스트
    void deleteNotice() {
        when(noticeRepository.findById(1L)).thenReturn(Optional.of(noticeEntity));

        noticeService.deleteNotice(1L);

        verify(noticeRepository, times(1)).findById(1L);
        verify(noticeRepository, times(1)).delete(noticeEntity);
    }
}
