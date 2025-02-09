package com.rsupport.project.notice.repository;

import com.rsupport.project.notice.entity.NoticeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class NoticeRepositoryTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Test // 공지사항 저장 테스트
    public void testSaveNotice() {
        // given
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .title("테스트 공지사항")
                .contents("테스트 공지 내용입니다.")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .author("테스트 작성자")
                .viewCount(0)
                .build();

        // when
        NoticeEntity savedNoticeEntity = noticeRepository.saveAndFlush(noticeEntity);

        // then
        assertThat(savedNoticeEntity).isNotNull();
        assertThat(savedNoticeEntity.getTitle()).isEqualTo("테스트 공지사항");
        assertThat(savedNoticeEntity.getContents()).isEqualTo("테스트 공지 내용입니다.");
        assertThat(savedNoticeEntity.getAuthor()).isEqualTo("테스트 작성자");
        assertThat(savedNoticeEntity.getViewCount()).isEqualTo(0);
    }

    @Test // 공지사항 저장 및 조회 테스트
    public void testSaveAndFindNotice() {
        // given
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .title("테스트 공지사항")
                .contents("테스트 공지 내용입니다.")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .author("테스트 작성자")
                .viewCount(0)
                .build();

        // when
        NoticeEntity savedNoticeEntity = noticeRepository.saveAndFlush(noticeEntity);
        Optional<NoticeEntity> foundNotice = noticeRepository.findById(savedNoticeEntity.getId());

        // then
        assertThat(foundNotice).isPresent();
        assertThat(foundNotice.get().getTitle()).isEqualTo("테스트 공지사항");
        assertThat(foundNotice.get().getContents()).isEqualTo("테스트 공지 내용입니다.");
        assertThat(foundNotice.get().getAuthor()).isEqualTo("테스트 작성자");
        assertThat(foundNotice.get().getViewCount()).isEqualTo(0);
    }

    @Test // 공지사항 수정 테스트
    public void testUpdateNotice() {
        // given
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .title("테스트 공지사항")
                .contents("테스트 공지 내용입니다.")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .author("테스트 작성자")
                .viewCount(0)
                .build();
        NoticeEntity savedNoticeEntity = noticeRepository.saveAndFlush(noticeEntity);

        // when
        NoticeEntity foundNotice = noticeRepository.findById(savedNoticeEntity.getId()).orElseThrow();
        foundNotice.setTitle("테스트 제목 수정");
        foundNotice.setContents("테스트 내용 수정");
        NoticeEntity updatedNotice = noticeRepository.saveAndFlush(foundNotice);

        // then
        Optional<NoticeEntity> reloadedNotice = noticeRepository.findById(updatedNotice.getId());

        assertThat(reloadedNotice).isPresent();
        assertThat(reloadedNotice.get().getTitle()).isEqualTo("테스트 제목 수정");
        assertThat(reloadedNotice.get().getContents()).isEqualTo("테스트 내용 수정");
        assertThat(reloadedNotice.get().getAuthor()).isEqualTo("테스트 작성자");

    }

    @Test // 공지사항 삭제 테스트
    public void testDeleteNotice() {
        // given
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .title("테스트 공지사항")
                .contents("테스트 공지 내용입니다.")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .author("테스트 작성자")
                .viewCount(0)
                .build();
        NoticeEntity savedNoticeEntity = noticeRepository.saveAndFlush(noticeEntity);

        // when
        noticeRepository.deleteById(savedNoticeEntity.getId());

        // then
        Optional<NoticeEntity> deletedNotice = noticeRepository.findById(noticeEntity.getId());
        assertThat(deletedNotice).isEmpty();
    }
}
