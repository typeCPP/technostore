package com.technostore.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.technostore.dto.FullProductDto;
import com.technostore.dto.OrderDto;
import com.technostore.dto.OrderStatus;
import com.technostore.dto.ProductDto;
import com.technostore.model.Order;
import com.technostore.model.OrderProduct;
import com.technostore.repository.OrderProductRepository;
import com.technostore.repository.OrderRepository;
import com.technostore.service.client.ProductRestTemplateClient;

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
        Order order = getOrCreateCurrentOrder(userId);

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
        Order order = getOrCreateCurrentOrder(userId);
        return fillOrderDtoByOrder(order, request);
    }

    @Override
    @Transactional
    public void completeOrder(Long userId, Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new EntityNotFoundException("No order with id = " + orderId);
        }
        if (!Objects.equals(orderOptional.get().getUserId(), userId)) {
            throw new EntityNotFoundException(
                    String.format("User with id = %s does not own order with id = %s", userId, orderId));
        }
        Order order = orderOptional.get();
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
    }

    @Override
    public List<Long> getCompletedOrdersIds(Long userId) {
        List<Order> orders = orderRepository.findOrdersByStatusEqualsAndUserId(OrderStatus.COMPLETED, userId);
        return orders.stream().map(Order::getId).toList();
    }

    @Override
    public OrderDto getCompletedOrder(Long orderId, Long userId, HttpServletRequest request) {
        Optional<Order> orderOptional = orderRepository.findOrderByIdAndStatus(orderId, OrderStatus.COMPLETED);
        if (orderOptional.isEmpty()) {
            throw new EntityNotFoundException("No completed order with id: " + orderId);
        }
        return fillOrderDtoByOrder(orderOptional.get(), request);
    }

    private Order getOrCreateCurrentOrder(Long userId) {
        Optional<Order> uncompleteOrderOptional = orderRepository
                .findOrderByStatusEqualsAndUserId(OrderStatus.IN_PROGRESS, userId);

        Order order;
        if (uncompleteOrderOptional.isEmpty()) {
            Order newOrder = new Order();
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

    private OrderDto fillOrderDtoByOrder(Order order, HttpServletRequest request) {
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
}
