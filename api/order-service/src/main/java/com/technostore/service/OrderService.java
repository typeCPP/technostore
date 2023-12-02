package com.technostore.service;

import com.technostore.dto.OrderDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderService {
    void setProductCount(Long userId, Long productId, int count);
    OrderDto getCurrentOrder(Long userId, HttpServletRequest request);
    void completeOrder(Long userId, Long orderId);
    List<Long> getCompletedOrdersIds(Long userId);
    OrderDto getCompletedOrder(Long orderId, Long userId, HttpServletRequest request);
    List<Long> getPopularProductsIds();
}
