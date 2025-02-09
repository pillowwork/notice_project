package com.rsupport.project.attachment.repository;

import com.rsupport.project.attachment.entity.AttachmentEntity;
import com.rsupport.project.notice.entity.NoticeEntity;
import com.rsupport.project.notice.repository.NoticeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AttachmentRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    private NoticeEntity noticeEntity;

    @BeforeEach
    public void setUp() {
        noticeEntity = NoticeEntity.builder()
                .title("테스트 공지")
                .contents("테스트 공지 내용입니다.")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .author("테스트 작성자")
                .viewCount(0)
                .build();
        noticeEntity = noticeRepository.saveAndFlush(noticeEntity);
    }

    @Test // 첨부 파일 저장 테스트
    public void testSaveAttachment() {
        // given
        AttachmentEntity attachment = AttachmentEntity.builder()
                .originalFileName("test.txt")
                .storedFileName("test_123.txt")
                .filePath("/uploads/test_123.txt")
                .fileSize(123L)
                .fileType("text/plain")
                .notice(noticeEntity)
                .build();

        // when
        AttachmentEntity savedAttachment = attachmentRepository.saveAndFlush(attachment);

        // then
        assertThat(savedAttachment).isNotNull();
        assertThat(savedAttachment.getOriginalFileName()).isEqualTo("test.txt");
        assertThat(savedAttachment.getStoredFileName()).isEqualTo("test_123.txt");
        assertThat(savedAttachment.getFilePath()).isEqualTo("/uploads/test_123.txt");
        assertThat(savedAttachment.getFileSize()).isEqualTo(123L);
        assertThat(savedAttachment.getFileType()).isEqualTo("text/plain");
        assertThat(savedAttachment.getNotice()).isNotNull();
        assertThat(savedAttachment.getNotice().getId()).isEqualTo(noticeEntity.getId());
    }

    @Test // 첨부 파일 저장 및 조회 테스트
    public void testSaveAndFindAttachment() {
        // given
        AttachmentEntity attachment = AttachmentEntity.builder()
                .originalFileName("test.txt")
                .storedFileName("test_123.txt")
                .filePath("/uploads/test_123.txt")
                .fileSize(123L)
                .fileType("text/plain")
                .notice(noticeEntity)
                .build();

        // when
        AttachmentEntity savedAttachment = attachmentRepository.saveAndFlush(attachment);
        Optional<AttachmentEntity> foundAttachment = attachmentRepository.findById(savedAttachment.getId());

        // then
        assertThat(foundAttachment).isPresent();
        assertThat(foundAttachment.get().getOriginalFileName()).isEqualTo("test.txt");
        assertThat(foundAttachment.get().getStoredFileName()).isEqualTo("test_123.txt");
        assertThat(foundAttachment.get().getFilePath()).isEqualTo("/uploads/test_123.txt");
        assertThat(foundAttachment.get().getFileSize()).isEqualTo(123L);
        assertThat(foundAttachment.get().getFileType()).isEqualTo("text/plain");
        assertThat(foundAttachment.get().getNotice()).isNotNull();
        assertThat(foundAttachment.get().getNotice().getId()).isEqualTo(noticeEntity.getId());
    }

    @Test // 특정 공지사항에 연결된 첨부 파일 조회 테스트
    public void testFindAttachmentsByNotice() {
        // given
        AttachmentEntity attachment1 = AttachmentEntity.builder()
                .originalFileName("test_1.txt")
                .storedFileName("test_1_123.txt")
                .filePath("/uploads/test_1_123.txt")
                .fileSize(123L)
                .fileType("text/plain")
                .notice(noticeEntity)
                .build();
        attachmentRepository.save(attachment1);

        AttachmentEntity attachment2 = AttachmentEntity.builder()
                .originalFileName("test_2.txt")
                .storedFileName("test_2_123.txt")
                .filePath("/uploads/test_2_123.txt")
                .fileSize(234L)
                .fileType("text/plain")
                .notice(noticeEntity)
                .build();
        attachmentRepository.save(attachment2);

        entityManager.flush();
        entityManager.clear();

        // when
        NoticeEntity foundNotice = noticeRepository.findById(noticeEntity.getId()).orElseThrow();
        List<AttachmentEntity> attachments = foundNotice.getAttachments();

        // then
        assertThat(attachments).isNotEmpty();
        assertThat(attachments).hasSize(2);
        assertThat(attachments.get(0).getOriginalFileName()).isEqualTo("test_1.txt");
        assertThat(attachments.get(1).getOriginalFileName()).isEqualTo("test_2.txt");
    }

    @Test // 첨부 파일 삭제 테스트
    public void testDeleteAttachment() {
        // given
        AttachmentEntity attachment = AttachmentEntity.builder()
                .originalFileName("test.txt")
                .storedFileName("test_123.txt")
                .filePath("/uploads/test_123.txt")
                .fileSize(123L)
                .fileType("text/plain")
                .notice(noticeEntity)
                .build();
        attachmentRepository.saveAndFlush(attachment);

        // when
        attachmentRepository.deleteById(attachment.getId());
        Optional<AttachmentEntity> deletedAttachment = attachmentRepository.findById(attachment.getId());

        // then
        assertThat(deletedAttachment).isEmpty();
    }

}
