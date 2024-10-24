package com.sparta.ssaktium.domain.likes.commentLikes.repository;

import com.sparta.ssaktium.domain.likes.commentLikes.entity.CommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikesRepository extends JpaRepository<CommentLikes,Long> {
    // 해당 유저가 해당 댓글에 좋아요 눌렀는지 확인
    boolean existsByCommentsIdAndUserId(Long commentsId, long userId);
}
