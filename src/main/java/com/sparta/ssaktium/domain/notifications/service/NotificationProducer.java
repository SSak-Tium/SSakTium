package com.sparta.ssaktium.domain.notifications.service;

import com.sparta.ssaktium.domain.notifications.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendNotification(NotificationMessage message) {
        kafkaTemplate.send("notifications", message);
    }
}