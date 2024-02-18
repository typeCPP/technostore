package com.technostore;

import com.technostore.dto.CategoryDto;
import com.technostore.dto.FullProductDto;
import com.technostore.dto.OrderStatus;
import com.technostore.dto.ReviewDto;
import com.technostore.model.Order;
import com.technostore.model.OrderProduct;
import com.technostore.service.client.ProductRestTemplateClient;
import com.technostore.service.client.UserRestTemplateClient;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class OrderTestFactory {

    public static Order buildOrder() {
        return Order.builder()
                .status(OrderStatus.IN_PROGRESS)
                .userId(1L)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    public static OrderProduct buildOrderProduct(Order order) {
        return OrderProduct.builder()
                .productId(1L)
                .order(order)
                .count(1)
                .build();
    }

    public static void mockUserService(UserRestTemplateClient userRestTemplateClient) {
        Mockito.when(userRestTemplateClient.getUserId(any()))
                .thenReturn(1L);
    }

    public static void mockProductService(ProductRestTemplateClient productRestTemplateClient, Long productId) {
        Mockito.when(productRestTemplateClient.getProductsByIds(eq(List.of(productId)), any()))
                .thenReturn(List.of(FullProductDto.builder()
                        .id(productId)
                        .price(100.0)
                        .linkPhoto("some url")
                        .name("some name")
                        .description("some desc")
                        .userRating(7.0)
                        .category(CategoryDto.builder().name("Ноутбуки").build())
                        .rating(5.0)
                        .reviews(List.of(
                                new ReviewDto(1, productId, "Nice review", 7, 1234567, "Nastia", "some url")
                        ))
                        .inCartCount(1)
                        .build()));
    }
}
