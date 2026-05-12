package com.fit.servicepayment_notification.service;

import com.fit.servicepayment_notification.dto.NotificationResponse;
import com.fit.servicepayment_notification.model.Notification;
import com.fit.servicepayment_notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationResponse createNotification(Long userId, Long orderId, String type, String message) {
        log.info("Creating notification for user {} - {}", userId, type);

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setOrderId(orderId);
        notification.setType(type);
        notification.setMessage(message);
        notification.setIsRead(false);

        Notification savedNotification = notificationRepository.save(notification);

        // Log notification for console display
        System.out.println("\n=== NOTIFICATION ===");
        System.out.println("User ID: " + userId);
        System.out.println("Order #: " + orderId);
        System.out.println("Type: " + type);
        System.out.println("Message: " + message);
        System.out.println("Time: " + savedNotification.getCreatedAt());
        System.out.println("===================\n");

        return convertToResponse(savedNotification);
    }

    public List<NotificationResponse> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getUnreadNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdAndIsRead(userId, false).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getNotificationsByOrderId(Long orderId) {
        return notificationRepository.findByOrderId(orderId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public NotificationResponse markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setIsRead(true);
        Notification updatedNotification = notificationRepository.save(notification);

        log.info("Notification {} marked as read", notificationId);
        return convertToResponse(updatedNotification);
    }

    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsRead(userId, false);
        notifications.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(notifications);
        log.info("All notifications marked as read for user {}", userId);
    }

    public NotificationResponse getNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        return convertToResponse(notification);
    }

    private NotificationResponse convertToResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getUserId(),
                notification.getOrderId(),
                notification.getType(),
                notification.getMessage(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }
}
