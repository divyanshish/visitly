package com.visitly.myproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEventPayload {
    private Long userId;
    private String email;
    private String username;
    private String eventType;
    private LocalDateTime timestamp;
}