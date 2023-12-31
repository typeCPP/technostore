package com.technostore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.technostore.model.Order;
import com.technostore.model.OrderProduct;
import org.springframework.data.jpa.repository.Query;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long>  {
    Optional<OrderProduct> findOrderProductByOrderAndProductId(Order order, Long productId);

    List<OrderProduct> findByOrder(Order order);

    @Query(value = "SELECT op.productId FROM OrderProduct op " +
            "GROUP BY op.productId " +
            "ORDER BY count(*) DESC")
    List<Long> findMostFrequentProductsIds(Pageable pageable);

    @Query(value = "SELECT op FROM OrderProduct op " +
            "JOIN op.order o " +
            "WHERE o.userId = :userId " +
            "AND op.productId in (:productIds)" +
            "AND o.status = 'IN_PROGRESS'")
    List<OrderProduct> findByUserIdAndProductIds(Long userId, List<Long> productIds);
}
