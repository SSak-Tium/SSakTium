package com.sparta.ssaktium.domain.likes.boardLikes.repository;

import com.sparta.ssaktium.domain.likes.boardLikes.entity.BoardLike;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    // 해당 유저가 해당 게시글에 좋아요 눌렀는지 확인
    boolean existsByBoardIdAndUserId(Long boardId, long userId);

    Optional<BoardLike> findByBoardIdAndUserId(Long boardId, Long userId);

    int countByBoardId(Long boardId);

    @Query("SELECT b.userId FROM BoardLike b WHERE b.board.id = :boardId")
    List<String> findUserIdsByBoardId(@Param("boardId")Long boardId);
}
