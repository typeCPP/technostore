package com.technostore;

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

import static com.technostore.OrderTestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

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
        long userId = 10000000L;
        Order order = orderRepository.save(buildOrder());
        OrderProduct orderProduct = orderProductRepository.save(buildOrderProduct(order));
        mockProductService(productRestTemplateClient, orderProduct.getProductId());

        OrderDto orderDto = orderService.getCurrentOrder(userId, null);
        assertThat(orderDto.getId()).isEqualTo(order.getId());
        assertThat(orderDto.getProducts().size()).isEqualTo(1);
        assertThat(orderDto.getProducts().get(0).getId()).isEqualTo(orderProduct.getProductId());
        assertThat(orderDto.getProducts().get(0).getName()).isEqualTo("some name");
    }
}
