package com.technostore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.technostore.model.Order;
import com.technostore.model.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long>  {
    Optional<OrderProduct> findOrderProductByOrderAndProductId(Order order, Long productId);

    List<OrderProduct> findByOrder(Order order);
}
