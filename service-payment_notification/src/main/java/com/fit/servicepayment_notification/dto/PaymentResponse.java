package com.fit.servicepayment_notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long orderId;
    private Long userId;
    private Double amount;
    private String paymentMethod;
    private String status;
    private String transactionId;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
