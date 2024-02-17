package com.technostore;

import com.technostore.dto.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

import com.technostore.dto.OrderDto;
import com.technostore.model.Order;
import com.technostore.model.OrderProduct;
import com.technostore.repository.OrderProductRepository;
import com.technostore.repository.OrderRepository;
import com.technostore.service.OrderService;
import com.technostore.service.client.ProductRestTemplateClient;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityNotFoundException;

import static com.technostore.OrderTestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderProductRepository orderProductRepository;
    @MockBean
    ProductRestTemplateClient productRestTemplateClient;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("DELETE FROM orders.order_product;");
        jdbcTemplate.execute("DELETE FROM orders.orders;");
    }

    @Test
    void getCurrentOrderTest() {
        long userId = 1L;
        Order order = orderRepository.save(buildOrder());
        OrderProduct orderProduct = orderProductRepository.save(buildOrderProduct(order));
        mockProductService(productRestTemplateClient, orderProduct.getProductId());

        OrderDto orderDto = orderService.getCurrentOrder(userId, null);
        assertThat(orderDto.getId()).isEqualTo(order.getId());
        assertThat(orderDto.getProducts().size()).isEqualTo(1);
        assertThat(orderDto.getProducts().get(0).getId()).isEqualTo(orderProduct.getProductId());
        assertThat(orderDto.getProducts().get(0).getName()).isEqualTo("some name");
    }

    @Test
    void completeOrderTest() {
        long userId = 1L;
        Order order = orderRepository.save(buildOrder());

        orderService.completeOrder(userId, order.getId());

        assertThat(orderRepository.findOrderByIdAndStatus(order.getId(), OrderStatus.COMPLETED)).isPresent();
    }

    @Test
    void tryCompleteOrderByAnotherUserTest() {
        long userId = 10000L;
        Order order = orderRepository.save(buildOrder());

        assertThatThrownBy(() -> orderService.completeOrder(userId, order.getId()))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(
                        String.format("User with id = %s does not own order with id = %s", userId, order.getId()));
    }

    @Test
    void setProductCountTest() {
        long userId = 1L;
        Order order = orderRepository.save(buildOrder());
        OrderProduct orderProduct = orderProductRepository.save(buildOrderProduct(order));
        mockProductService(productRestTemplateClient, orderProduct.getProductId());

        orderService.setProductCount(userId, orderProduct.getProductId(), 10);
        assertThat(orderProductRepository.findByOrder(order).get(0).getCount()).isEqualTo(10);
    }

    @Test
    void setProductCountWhenCountLessOneTest() {
        long userId = 1L;
        Order order = orderRepository.save(buildOrder());
        OrderProduct orderProduct = orderProductRepository.save(buildOrderProduct(order));

        orderService.setProductCount(userId, orderProduct.getProductId(), 0);
        assertThat(orderProductRepository.findByOrder(order).size()).isEqualTo(0);
    }

    @Test
    void setProductCountWhenOrderNotExistOneTest() {
        long userId = 1L;

        orderService.setProductCount(userId, 1L, 45);
        
        Order order = orderRepository.findOrdersByStatusEqualsAndUserId(OrderStatus.IN_PROGRESS, userId).get(0);
        assertThat(orderProductRepository.findByOrder(order).size()).isEqualTo(1);
        assertThat(orderProductRepository.findByOrder(order).get(0).getProductId()).isEqualTo(1);
        assertThat(orderProductRepository.findByOrder(order).get(0).getCount()).isEqualTo(45);
    }
}
