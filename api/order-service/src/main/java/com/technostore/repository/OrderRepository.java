package com.technostore.repository;

import com.technostore.dto.OrderStatus;
import com.technostore.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findOrderEntityByStatusEqualsAndUserId(OrderStatus status, Long userId);
}
