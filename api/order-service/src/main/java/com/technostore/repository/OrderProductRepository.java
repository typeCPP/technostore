package com.technostore.repository;

import com.technostore.model.OrderEntity;
import com.technostore.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long>  {
    Optional<OrderProduct> findOrderProductByOrderAndProductId(OrderEntity order, Long productId);

    List<OrderProduct> findByOrder(OrderEntity order);
}
