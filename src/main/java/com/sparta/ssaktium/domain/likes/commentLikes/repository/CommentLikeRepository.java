package com.sparta.ssaktium.domain.likes.commentLikes.repository;

import com.sparta.ssaktium.domain.likes.commentLikes.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    // 해당 유저가 해당 댓글에 좋아요 눌렀는지 확인
    boolean existsByCommentIdAndUserId(Long commentsId, long userId);

    Optional<CommentLike> findByCommentIdAndUserId(Long commentId, Long userId);

    int countByCommentId(Long l);

    List<String> findUserIdsByCommentId(long L);
}
