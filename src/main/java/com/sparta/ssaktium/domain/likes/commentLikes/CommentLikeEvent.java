package com.sparta.ssaktium.domain.likes.commentLikes;

import lombok.NoArgsConstructor;

// 좋아요 데이터
@NoArgsConstructor
public class CommentLikeEvent {

    private String userId;
    private String commentId;
    private String eventType; // LIKE 좋아요 , CANCEL 좋아요 취소

    public CommentLikeEvent(String userId, String commentId, String eventType) {
        this.userId = userId;
        this.commentId = commentId;
        this.eventType = eventType;
    }

    public String getUserId() {
        return userId;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getEventType() {
        return eventType;
    }
}