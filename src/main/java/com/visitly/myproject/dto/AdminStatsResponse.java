package com.visitly.myproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminStatsResponse {
    private Long totalUsers;
    private Long activeUsers;
    private Map<Long, LocalDateTime> lastLoginTimes;
    private LocalDateTime timestamp;
}