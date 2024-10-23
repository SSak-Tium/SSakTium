package com.sparta.springusersetting.domain.likes.boardLikes.repository;

import com.sparta.springusersetting.domain.likes.boardLikes.entity.BoardLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikesRepository extends JpaRepository<BoardLikes, Long> {
    int countByBoardId(Long boardId); // 게시글의 좋아요 수 조회
}
