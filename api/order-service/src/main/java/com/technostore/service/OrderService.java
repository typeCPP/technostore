package com.technostore.service;

import com.technostore.dto.OrderDto;

import javax.servlet.http.HttpServletRequest;

public interface OrderService {
    void setProductCount(Long userId, Long productId, int count);
    OrderDto getCurrentOrder(Long userId, HttpServletRequest request);
    void completeOrder(Long userId, Long orderId);
}
