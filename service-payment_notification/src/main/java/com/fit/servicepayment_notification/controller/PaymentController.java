package com.fit.servicepayment_notification.controller;

import com.fit.servicepayment_notification.dto.PaymentRequest;
import com.fit.servicepayment_notification.dto.PaymentResponse;
import com.fit.servicepayment_notification.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class PaymentController {
    private final PaymentService paymentService;

    /**
     * Create a new payment
     * POST /api/payments
     */
    @PostMapping
    public ResponseEntity<?> createPayment(@Valid @RequestBody PaymentRequest request) {
        try {
            log.info("Creating payment for order: {}", request.getOrderId());
            PaymentResponse response = paymentService.createPayment(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "Payment created successfully",
                            "data", response
                    ));
        } catch (Exception e) {
            log.error("Error creating payment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Process a payment
     * POST /api/payments/:id/process
     */
    @PostMapping("/{id}/process")
    public ResponseEntity<?> processPayment(@PathVariable Long id) {
        try {
            log.info("Processing payment: {}", id);
            PaymentResponse response = paymentService.processPayment(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Payment processed successfully",
                    "data", response
            ));
        } catch (Exception e) {
            log.error("Error processing payment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get payment by ID
     * GET /api/payments/:id
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPayment(@PathVariable Long id) {
        try {
            PaymentResponse response = paymentService.getPayment(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get payments by user ID
     * GET /api/payments/user/:userId
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByUserId(@PathVariable Long userId) {
        List<PaymentResponse> payments = paymentService.getPaymentsByUserId(userId);
        return ResponseEntity.ok(payments);
    }

    /**
     * Get payments by order ID
     * GET /api/payments/order/:orderId
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByOrderId(@PathVariable Long orderId) {
        List<PaymentResponse> payments = paymentService.getPaymentsByOrderId(orderId);
        return ResponseEntity.ok(payments);
    }

    /**
     * Cancel payment
     * PUT /api/payments/:id/cancel
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelPayment(@PathVariable Long id) {
        try {
            log.info("Cancelling payment: {}", id);
            PaymentResponse response = paymentService.cancelPayment(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Payment cancelled successfully",
                    "data", response
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
