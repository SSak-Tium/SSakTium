package com.sparta.ssaktium.domain.notifications.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
    private Long userId;
    private EventType eventType;
    private String message;
}