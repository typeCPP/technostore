package com.technostore;

import com.technostore.dto.CategoryDto;
import com.technostore.dto.FullProductDto;
import com.technostore.dto.OrderDto;
import com.technostore.dto.OrderStatus;
import com.technostore.model.Order;
import com.technostore.model.OrderProduct;
import com.technostore.repository.OrderProductRepository;
import com.technostore.repository.OrderRepository;
import com.technostore.service.OrderService;
import com.technostore.service.client.ProductRestTemplateClient;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderProductRepository orderProductRepository;
    @MockBean
    ProductRestTemplateClient productRestTemplateClient;

    @Test
    void getCurrentOrderTest() {
        Long userId = 10000000L;
        Long productId = 1L;
        Order order = orderRepository.save(Order.builder()
                .status(OrderStatus.IN_PROGRESS)
                .userId(userId)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build()
        );

        OrderProduct orderProduct = orderProductRepository.save(OrderProduct.builder()
                .productId(1L)
                .order(order)
                .count(1)
                .build());

        Mockito.when(productRestTemplateClient.getProductsByIds(eq(List.of(orderProduct.getProductId())), any()))
                .thenReturn(List.of(FullProductDto.builder()
                        .id(productId)
                        .price(100.0)
                        .linkPhoto("some url")
                        .name("some name")
                        .description("some desc")
                        .userRating(7.0)
                        .category(CategoryDto.builder().name("Ноутбуки").build())
                        .rating(5.0)
                        .inCartCount(1)
                        .build()));

        OrderDto orderDto = orderService.getCurrentOrder(userId, null);
        assertThat(orderDto.getId()).isEqualTo(order.getId());
        assertThat(orderDto.getProducts().size()).isEqualTo(1);
        assertThat(orderDto.getProducts().get(0).getId()).isEqualTo(orderProduct.getProductId());
        assertThat(orderDto.getProducts().get(0).getName()).isEqualTo("some name");
    }
}
