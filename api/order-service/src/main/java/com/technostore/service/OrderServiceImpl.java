package com.technostore.service;

import com.technostore.dto.FullProductDto;
import com.technostore.dto.OrderDto;
import com.technostore.dto.OrderStatus;
import com.technostore.dto.ProductDto;
import com.technostore.model.OrderEntity;
import com.technostore.model.OrderProduct;
import com.technostore.repository.OrderProductRepository;
import com.technostore.repository.OrderRepository;
import com.technostore.service.client.ProductRestTemplateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    ProductRestTemplateClient productRestTemplateClient;

    @Override
    public void setProductCount(Long userId, Long productId, int count) {
        OrderEntity order = getOrCreateCurrentOrder(userId);

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProductId(productId);
        orderProduct.setOrder(order);
        orderProduct.setCount(count);

        Optional<OrderProduct> orderProductOptional = orderProductRepository
                .findOrderProductByOrderAndProductId(order, productId);
        orderProductOptional.ifPresent(product -> orderProduct.setId(product.getId()));

        if (count < 1) {
            orderProductRepository.delete(orderProduct);
        } else {
            orderProductRepository.save(orderProduct);
        }
    }

    @Override
    public OrderDto getCurrentOrder(Long userId, HttpServletRequest request) {
        OrderEntity order = getOrCreateCurrentOrder(userId);
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());

        List<OrderProduct> products = orderProductRepository.findByOrder(order);
        if (products.isEmpty()) {

            return orderDto;
        }
        List<Long> productIds = products.stream().map(OrderProduct::getProductId).toList();
        List<FullProductDto> fullProductsList = productRestTemplateClient.getProductsByIds(productIds, request);

        Map<Long, Integer> productIdToCountMap = new HashMap<>();
        for (OrderProduct orderProduct : products) {
            productIdToCountMap.put(orderProduct.getProductId(), orderProduct.getCount());
        }

        List<ProductDto> productDtoList = fullProductsList.stream().map(fullProductDto -> ProductDto.builder()
                .id(fullProductDto.getId())
                .name(fullProductDto.getName())
                .price(fullProductDto.getPrice())
                .linkPhoto(fullProductDto.getLinkPhoto())
                .rating(fullProductDto.getRating())
                .count(productIdToCountMap.get(fullProductDto.getId()))
                .build()).toList();
        orderDto.setProducts(productDtoList);
        return orderDto;
    }

    private OrderEntity getOrCreateCurrentOrder(Long userId) {
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

        return order;
    }
}
