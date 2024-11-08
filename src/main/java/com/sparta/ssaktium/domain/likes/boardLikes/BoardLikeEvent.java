package com.sparta.ssaktium.domain.likes.boardLikes;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 좋아요 데이터
@Getter
@NoArgsConstructor
public class BoardLikeEvent {

    private String userId;
    private String boardId;
    private String eventType; // LIKE 좋아요 , CANCEL 좋아요 취소

    public BoardLikeEvent(String userId, String boardId, String eventType) {
        this.userId = userId;
        this.boardId = boardId;
        this.eventType = eventType;
    }

    public String getUserId() {
        return userId;
    }

    public String getBoardId() {
        return boardId;
    }

    public String getEventType() {
        return eventType;
    }
}