package com.sparta.ssaktium.domain.notification.notificationMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {

    private Long userId;
    private String eventType;
    private String message;

}