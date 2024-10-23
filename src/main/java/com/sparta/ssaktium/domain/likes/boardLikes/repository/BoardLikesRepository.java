package com.sparta.ssaktium.domain.likes.boardLikes.repository;

import com.sparta.ssaktium.domain.likes.boardLikes.entity.BoardLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikesRepository extends JpaRepository<BoardLikes, Long> {
    int countByBoardId(Long boardId); // 게시글의 좋아요 수 조회

    boolean existsByBoardIdandUserId(Long boardId, long userId); // 해당 유저가 해당 게시글에 좋아요 눌렀는지 확인
}
