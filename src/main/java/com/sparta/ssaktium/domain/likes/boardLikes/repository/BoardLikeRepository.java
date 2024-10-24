package com.sparta.ssaktium.domain.likes.boardLikes.repository;

import com.sparta.ssaktium.domain.likes.boardLikes.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    // 게시글의 좋아요 수 조회
    int countByBoardId(Long boardId);

    // 해당 유저가 해당 게시글에 좋아요 눌렀는지 확인
    boolean existsByBoardIdAndUserId(Long boardId, long userId);
}
