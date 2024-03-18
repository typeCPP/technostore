package com.technostore.integration;

import java.util.List;
import java.util.Map;

import com.technostore.OrderTestFactory;
import com.technostore.controller.OrderController;
import com.technostore.dto.OrderStatus;
import com.technostore.model.Order;
import com.technostore.model.OrderProduct;
import com.technostore.repository.OrderProductRepository;
import com.technostore.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static com.technostore.OrderTestFactory.*;
import static com.technostore.TestUtils.getFileContent;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {
    private final static String JWT
            = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI0NWQwN2E2YzYzZmU0Y2EwYjgxZmU1NzhkNTQ1ZWJkYiIsInN1YiI6Iml2YW5vdmEuYUB5YW5kZXgucnUiLCJpYXQiOjE3MTAwMTg1MDgsImV4cCI6MTc0MTU1NDUwOH0.jevXRK5k0sFz1Dcalj_tigqsusLvMkmII4JpG9_zLEPdZZZYPECBtdTHBoXWdIqcIk_ASWGEynl_I9chuDA5WA";
    private final static long userId = 1L;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderProductRepository orderProductRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    OrderController orderController;
    @Autowired
    RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("DELETE FROM orders.order_product;");
        jdbcTemplate.execute("DELETE FROM orders.orders;");
        jdbcTemplate.execute("DELETE FROM product.category;");
        jdbcTemplate.execute("DELETE FROM product.product;");
    }

    @DisplayName("Получение текущей корзины с товарами")
    @Test
    void getCurrentOrderTest() throws Exception {

        Order order = orderRepository.save(OrderTestFactory.buildOrder(userId));
        List<OrderProduct> orderProducts = orderProductRepository.saveAll(OrderTestFactory.buildOrderProducts(order));
        createProducts();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + JWT);

        mockMvc.perform(get("/order/get-current-order").headers(headers))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(String.format(getFileContent("integration/order.json"),
                        order.getId()), true));
    }

    @DisplayName("Оформление заказа пользователя")
    @Test
    void completeOrderTest() throws Exception {
        Order order = orderRepository.save(OrderTestFactory.buildOrder(userId));
        List<OrderProduct> orderProducts = orderProductRepository.saveAll(OrderTestFactory.buildOrderProducts(order));
        createProducts();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + JWT);

        mockMvc.perform(post("/order/complete-order/" + order.getId()).headers(headers))
                .andExpect(status().is2xxSuccessful());
        assertThat(orderRepository.findById(order.getId()).get().getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @DisplayName("Получение всех завершенных заказов пользователя")
    @Test
    void getCompletedOrderTest() throws Exception {
        Order completedOrder1 = OrderTestFactory.buildOrder(userId);
        completedOrder1.setStatus(OrderStatus.COMPLETED);
        Order completedOrder2 = OrderTestFactory.buildOrder(userId);
        completedOrder2.setStatus(OrderStatus.COMPLETED);
        Order notCompletedOrder = OrderTestFactory.buildOrder(userId);
        notCompletedOrder.setStatus(OrderStatus.IN_PROGRESS);
        orderRepository.saveAll(List.of(completedOrder1, completedOrder2, notCompletedOrder));
        createProducts();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + JWT);

        mockMvc.perform(get("/order/get-completed-orders").headers(headers))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(String.format("{\"ids\":[%s,%s]}", completedOrder1.getId(), completedOrder2.getId())));
    }

    @DisplayName("Получение количества товаров в корзине по id товаров")
    @Test
    void getInCartCountByProductsIdsTest() throws Exception {
        Order order = orderRepository.save(buildOrder(userId));
        OrderProduct orderProduct = orderProductRepository.save(buildOrderProduct(order));
        orderProductRepository.save(OrderProduct.builder()
                .productId(2L)
                .order(order)
                .count(100)
                .build());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + JWT);

        mockMvc.perform(get("/order/get-in-cart-count-by-product-ids?ids=1,2").headers(headers))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[*]", containsInAnyOrder(
                        Map.of("productId", 1, "inCartCount", 1),
                        Map.of("productId", 2, "inCartCount", 100)
                )));
    }

    private void createProducts() {
        jdbcTemplate.execute("""
                insert into product.category (id, name) values (1, 'Ноутбуки');
                """);
        jdbcTemplate.execute("""
                insert into product.product (id, description, link_photo, name, price, category_id)
                values (1, 'Хороший ноутбук', 'some url', 'Макбук', '100000', 1)
                """);
        jdbcTemplate.execute("""
                insert into product.product (id, description, link_photo, name, price, category_id)
                values (2, 'Плохой ноутбук', 'some url 2', 'Старый Макбук', '1000', 1)
                """);
    }
}
