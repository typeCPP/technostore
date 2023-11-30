package com.technostore.service;

import com.technostore.dto.OrderStatus;
import com.technostore.model.OrderEntity;
import com.technostore.model.OrderProduct;
import com.technostore.repository.OrderProductRepository;
import com.technostore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Override
    public void setProductCount(Long userId, Long productId, int count) {
        Optional<OrderEntity> uncompleteOrderOptional = orderRepository
                .findOrderEntityByStatusEqualsAndUserId(OrderStatus.IN_PROGRESS, userId);

        OrderEntity order;
        if (uncompleteOrderOptional.isEmpty()) {
            OrderEntity newOrder = new OrderEntity();
            Instant instantNow = Instant.now();
            newOrder.setUserId(userId);
            newOrder.setStatus(OrderStatus.IN_PROGRESS);
            newOrder.setCreatedAt(instantNow);
            newOrder.setUpdatedAt(instantNow);
            order = orderRepository.save(newOrder);
        } else {
            order = uncompleteOrderOptional.get();
            order.setUpdatedAt(Instant.now());
            orderRepository.save(order);
        }

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProductId(productId);
        orderProduct.setOrder(order);
        orderProduct.setCount(count);

        Optional<OrderProduct> orderProductOptional = orderProductRepository
                .findOrderProductByOrderAndProductId(order, productId);
        orderProductOptional.ifPresent(product -> orderProduct.setId(product.getId()));

        orderProductRepository.save(orderProduct);
    }
}
