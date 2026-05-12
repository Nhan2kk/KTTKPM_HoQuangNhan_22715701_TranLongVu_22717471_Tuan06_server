package com.fit.servicepayment_notification.controller;

import com.fit.servicepayment_notification.dto.NotificationResponse;
import com.fit.servicepayment_notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class NotificationController {
    private final NotificationService notificationService;

    /**
     * Get notifications by user ID
     * GET /api/notifications/user/:userId
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByUserId(@PathVariable Long userId) {
        log.info("Fetching notifications for user: {}", userId);
        List<NotificationResponse> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get unread notifications by user ID
     * GET /api/notifications/user/:userId/unread
     */
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(@PathVariable Long userId) {
        log.info("Fetching unread notifications for user: {}", userId);
        List<NotificationResponse> notifications = notificationService.getUnreadNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get notifications by order ID
     * GET /api/notifications/order/:orderId
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByOrderId(@PathVariable Long orderId) {
        log.info("Fetching notifications for order: {}", orderId);
        List<NotificationResponse> notifications = notificationService.getNotificationsByOrderId(orderId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get a specific notification
     * GET /api/notifications/:id
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getNotification(@PathVariable Long id) {
        try {
            NotificationResponse notification = notificationService.getNotification(id);
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Mark notification as read
     * PUT /api/notifications/:id/read
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        try {
            log.info("Marking notification as read: {}", id);
            NotificationResponse notification = notificationService.markAsRead(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Notification marked as read",
                    "data", notification
            ));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Mark all notifications as read for a user
     * PUT /api/notifications/user/:userId/read-all
     */
    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<?> markAllAsRead(@PathVariable Long userId) {
        try {
            log.info("Marking all notifications as read for user: {}", userId);
            notificationService.markAllAsRead(userId);
            return ResponseEntity.ok(Map.of(
                    "message", "All notifications marked as read"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
