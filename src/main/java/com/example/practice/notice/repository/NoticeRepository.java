package com.example.practice.notice.repository;

import com.example.practice.notice.entity.Notice;
import com.example.practice.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<List<Notice>> findByIdIn(List<Long> idList);

    Optional<List<Notice>> findByTitleAndContentsAndRegDateIsGreaterThanEqual(String title, String contents, LocalDateTime regDate);

    int countByTitleAndContentsAndRegDateIsGreaterThanEqual(String title, String contents, LocalDateTime regDate);

    List<Notice> findByUser(User user);

    long countByUser(User user);
}
