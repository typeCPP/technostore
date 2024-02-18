package com.technostore;

import com.technostore.dto.InCartCountProductDto;
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

import java.util.List;

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
    void getCompletedOrderTest() {
        long userId = 1L;
        Order order = orderRepository.save(buildOrder());
        OrderProduct orderProduct = orderProductRepository.save(buildOrderProduct(order));
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
        mockProductService(productRestTemplateClient, orderProduct.getProductId());

        OrderDto orderDto = orderService.getCompletedOrder(order.getId(), userId, null);
        assertThat(orderDto.getId()).isEqualTo(order.getId());
        assertThat(orderDto.getProducts().size()).isEqualTo(1);
        assertThat(orderDto.getProducts().get(0).getId()).isEqualTo(orderProduct.getProductId());
        assertThat(orderDto.getProducts().get(0).getName()).isEqualTo("some name");
    }

    @Test
    void tryGetCompletedOrderButOrderNotExistsUserTest() {
        long userId = 1L;
        assertThatThrownBy(() -> orderService.getCompletedOrder(1000000L, userId, null))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage("No completed order with id: 1000000");
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

    @Test
    void getPopularProductsIdsTest() {
        Order order = orderRepository.save(buildOrder());
        OrderProduct orderProduct = orderProductRepository.save(buildOrderProduct(order));
        Order order2 = orderRepository.save(buildOrder());
        OrderProduct orderProduct2 = orderProductRepository.save(
                OrderProduct.builder()
                        .productId(2L)
                        .order(order2)
                        .count(1)
                        .build());
        OrderProduct orderProduct3 = orderProductRepository.save(
                OrderProduct.builder()
                        .productId(2L)
                        .order(order)
                        .count(1)
                        .build());
        mockProductService(productRestTemplateClient, orderProduct.getProductId());

        List<Long> productsIds = orderService.getPopularProductsIds();
        assertThat(productsIds.size()).isEqualTo(2);
        assertThat(productsIds).containsExactly(2L, 1L);
    }

    @Test
    void getInCartCountByProductsIdsTest() {
        Order order = orderRepository.save(buildOrder());
        OrderProduct orderProduct = orderProductRepository.save(buildOrderProduct(order));
        OrderProduct orderProduct2 = orderProductRepository.save(
                OrderProduct.builder()
                        .productId(2L)
                        .order(order)
                        .count(100)
                        .build());

        List<InCartCountProductDto> inCartCountByProductIds
                = orderService.getInCartCountByProductIds(List.of(1L, 2L), 1L);
        assertThat(inCartCountByProductIds.size()).isEqualTo(2);
        assertThat(inCartCountByProductIds.stream().map(InCartCountProductDto::getProductId))
                .containsExactlyInAnyOrder(1L, 2L);
    }

}
