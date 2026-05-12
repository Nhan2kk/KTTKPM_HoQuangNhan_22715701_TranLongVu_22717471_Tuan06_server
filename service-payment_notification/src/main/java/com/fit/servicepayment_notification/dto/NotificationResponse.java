package com.fit.servicepayment_notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private Long userId;
    private Long orderId;
    private String type;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
