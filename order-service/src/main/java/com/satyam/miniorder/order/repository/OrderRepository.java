package com.satyam.miniorder.order.repository;

import com.satyam.miniorder.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
