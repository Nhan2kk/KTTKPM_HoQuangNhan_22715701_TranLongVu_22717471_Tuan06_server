package com.fit.servicepayment_notification.repository;

import com.fit.servicepayment_notification.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrderId(Long orderId);

    List<Payment> findByUserId(Long userId);

    Optional<Payment> findByOrderIdAndStatus(Long orderId, String status);

    List<Payment> findByStatus(String status);
}
