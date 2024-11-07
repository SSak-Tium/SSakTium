package com.sparta.ssaktium.domain.likes;

import lombok.NoArgsConstructor;

// 좋아요 데이터
@NoArgsConstructor
public class LikeEvent {

    private String userId;
    private String boardId;
    private String eventType;

    public LikeEvent(String userId, String boardId, String eventType) {
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