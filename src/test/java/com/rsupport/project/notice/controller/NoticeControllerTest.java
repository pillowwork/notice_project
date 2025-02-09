package com.rsupport.project.notice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsupport.project.notice.dto.NoticeDto;
import com.rsupport.project.notice.entity.NoticeEntity;
import com.rsupport.project.notice.service.NoticeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class NoticeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NoticeService noticeService;

    @InjectMocks
    private NoticeController noticeController;

    private NoticeDto.Request createRequestDto;
    private NoticeDto.Response createResponseDto;
    private NoticeDto.Response updateResponseDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(noticeController).build();

        createRequestDto = new NoticeDto.Request();
        createRequestDto.setTitle("테스트 공지사항");
        createRequestDto.setContents("테스트 공지 내용입니다.");
        createRequestDto.setAuthor("테스트 작성자");

        NoticeEntity createResponseEntity = NoticeEntity.builder()
                .id(1L)
                .title("테스트 공지사항")
                .contents("테스트 공지 내용입니다.")
                .author("테스트 작성자")
                .build();
        createResponseDto = new NoticeDto.Response(createResponseEntity);

        NoticeEntity updateResponseEntity = NoticeEntity.builder()
                .id(1L)
                .title("수정된 공지사항")
                .contents("수정된 공지 내용입니다.")
                .author("수정된 작성자")
                .build();
        updateResponseDto = new NoticeDto.Response(updateResponseEntity);
        
    }

    @Test // 공지사항 등록 테스트
    void testCreateNotice() throws Exception {
        when(noticeService.createNotice(any(NoticeDto.Request.class))).thenReturn(createResponseDto);

        MockMultipartFile file = new MockMultipartFile("attachments", "test.txt", "text/plain", "test file content".getBytes());

        mockMvc.perform(multipart("/api/notice")
                        .file(file)
                        .param("title", "테스트 공지사항")
                        .param("contents", "테스트 공지 내용입니다.")
                        .param("author", "테스트 작성자")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("테스트 공지사항"));

        verify(noticeService, times(1)).createNotice(any(NoticeDto.Request.class));
    }

    @Test // 공지사항 조회 테스트
    void testReadNotice() throws Exception {
        when(noticeService.readNotice(1L)).thenReturn(Optional.of(createResponseDto));

        mockMvc.perform(get("/api/notice/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("테스트 공지사항"));

        verify(noticeService, times(1)).readNotice(1L);
    }

    @Test // 공지사항 수정 테스트
    void testUpdateNotice() throws Exception {
        when(noticeService.updateNotice(anyLong(), any(NoticeDto.Request.class))).thenReturn(updateResponseDto);

        MockMultipartFile file = new MockMultipartFile("attachments", "test-update.txt", "text/plain", "updated file content".getBytes());

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/notice/1")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("title", "수정된 공지사항")
                        .param("contents", "수정된 공지 내용입니다.")
                        .param("author", "수정된 작성자"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 공지사항"));

        verify(noticeService, times(1)).updateNotice(anyLong(), any(NoticeDto.Request.class));
    }

    @Test // 공지사항 삭제 테스트
    void testDeleteNotice() throws Exception {
        doNothing().when(noticeService).deleteNotice(1L);

        mockMvc.perform(delete("/api/notice/1"))
                .andExpect(status().isNoContent());

        verify(noticeService, times(1)).deleteNotice(1L);
    }

}
