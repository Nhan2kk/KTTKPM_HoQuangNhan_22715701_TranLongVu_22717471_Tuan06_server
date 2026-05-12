package com.fit.servicepayment_notification.repository;

import com.fit.servicepayment_notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);

    List<Notification> findByUserIdAndIsRead(Long userId, Boolean isRead);

    List<Notification> findByOrderId(Long orderId);

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
}
