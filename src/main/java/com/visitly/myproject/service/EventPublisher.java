package com.visitly.myproject.service;
import com.visitly.myproject.config.RabbitMQConfig;
import com.visitly.myproject.dto.UserEventPayload;
import com.visitly.myproject.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishRegistrationEvent(User user) {
        UserEventPayload payload = UserEventPayload.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .eventType("USER_REGISTERED")
                .timestamp(LocalDateTime.now())
                .build();

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EVENTS_EXCHANGE,
                    RabbitMQConfig.REGISTRATION_ROUTING_KEY,
                    payload
            );
            log.info("Registration event published for user: {}", user.getId());
        } catch (Exception e) {
            log.error("Failed to publish registration event: {}", e.getMessage());
        }
    }

    public void publishLoginEvent(User user) {
        UserEventPayload payload = UserEventPayload.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .eventType("USER_LOGIN")
                .timestamp(LocalDateTime.now())
                .build();

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EVENTS_EXCHANGE,
                    RabbitMQConfig.LOGIN_ROUTING_KEY,
                    payload
            );
            log.info("Login event published for user: {}", user.getId());
        } catch (Exception e) {
            log.error("Failed to publish login event: {}", e.getMessage());
        }
    }
}