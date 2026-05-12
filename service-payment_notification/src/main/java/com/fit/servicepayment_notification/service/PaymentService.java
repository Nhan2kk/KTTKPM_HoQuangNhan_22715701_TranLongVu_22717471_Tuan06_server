package com.fit.servicepayment_notification.service;

import com.fit.servicepayment_notification.dto.PaymentRequest;
import com.fit.servicepayment_notification.dto.PaymentResponse;
import com.fit.servicepayment_notification.model.Payment;
import com.fit.servicepayment_notification.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    private final RestTemplate restTemplate;

    public PaymentResponse createPayment(PaymentRequest request) {
        log.info("Creating payment for order {} with method {}", request.getOrderId(), request.getPaymentMethod());

        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setUserId(request.getUserId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setDescription(request.getDescription());
        payment.setStatus("PENDING");

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment created with ID: {}", savedPayment.getId());

        return convertToResponse(savedPayment);
    }

    public PaymentResponse processPayment(Long paymentId) {
        log.info("Processing payment with ID: {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!payment.getStatus().equals("PENDING")) {
            throw new RuntimeException("Payment is already processed");
        }

        payment.setStatus("PROCESSING");
        paymentRepository.save(payment);

        // Simulate payment processing
        try {
            simulatePaymentProcessing(payment);
            payment.setStatus("SUCCESS");
            payment.setTransactionId("TXN-" + UUID.randomUUID().toString());
            payment.setCompletedAt(LocalDateTime.now());
            paymentRepository.save(payment);

            // Update order status
            updateOrderStatus(payment.getOrderId(), "CONFIRMED");

            // Send notification
            notificationService.createNotification(
                    payment.getUserId(),
                    payment.getOrderId(),
                    "PAYMENT_SUCCESS",
                    "Payment successful! Your order #" + payment.getOrderId() + " is confirmed."
            );

            log.info("Payment processed successfully: {}", paymentId);
        } catch (Exception e) {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            log.error("Payment processing failed: {}", e.getMessage());
            throw new RuntimeException("Payment processing failed: " + e.getMessage());
        }

        return convertToResponse(payment);
    }

    public PaymentResponse getPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return convertToResponse(payment);
    }

    public List<PaymentResponse> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public PaymentResponse cancelPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus().equals("SUCCESS")) {
            throw new RuntimeException("Cannot cancel a successful payment");
        }

        payment.setStatus("CANCELLED");
        paymentRepository.save(payment);

        log.info("Payment cancelled: {}", paymentId);
        return convertToResponse(payment);
    }

    private void simulatePaymentProcessing(Payment payment) throws InterruptedException {
        // Simulate payment processing delay
        Thread.sleep(1000);
        // In a real scenario, integrate with actual payment gateway
    }

    private void updateOrderStatus(Long orderId, String status) {
        try {
            log.info("Updating order {} status to {}", orderId, status);
            String url = "http://localhost:8083/api/orders/" + orderId + "/status";
            restTemplate.put(url, java.util.Map.of("status", status));
        } catch (Exception e) {
            log.error("Failed to update order status: {}", e.getMessage());
            // Continue without throwing - order update is non-critical
        }
    }

    private PaymentResponse convertToResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getUserId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getTransactionId(),
                payment.getDescription(),
                payment.getCreatedAt(),
                payment.getCompletedAt()
        );
    }
}
