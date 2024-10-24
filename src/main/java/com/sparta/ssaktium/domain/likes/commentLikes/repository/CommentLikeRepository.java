package com.sparta.ssaktium.domain.likes.commentLikes.repository;

import com.sparta.ssaktium.domain.likes.commentLikes.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long> {
    // 해당 유저가 해당 댓글에 좋아요 눌렀는지 확인
    boolean existsByCommentIdAndUserId(Long commentsId, long userId);
}
