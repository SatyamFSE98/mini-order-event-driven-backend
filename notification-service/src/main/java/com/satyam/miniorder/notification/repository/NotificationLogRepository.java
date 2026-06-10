package com.satyam.miniorder.notification.repository;

import com.satyam.miniorder.notification.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    List<NotificationLog> findByOrderId(Long orderId);
}
