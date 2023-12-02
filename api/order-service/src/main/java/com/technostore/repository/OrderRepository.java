package com.technostore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technostore.dto.OrderStatus;
import com.technostore.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderByStatusEqualsAndUserId(OrderStatus status, Long userId);
}
