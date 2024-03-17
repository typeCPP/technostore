package com.technostore.integration;

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
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static com.technostore.TestUtils.getFileContent;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {
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
        long userId = 1;
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI0NWQwN2E2YzYzZmU0Y2EwYjgxZmU1NzhkNTQ1ZWJkYiIsInN1YiI6Iml2YW5vdmEuYUB5YW5kZXgucnUiLCJpYXQiOjE3MTAwMTg1MDgsImV4cCI6MTc0MTU1NDUwOH0.jevXRK5k0sFz1Dcalj_tigqsusLvMkmII4JpG9_zLEPdZZZYPECBtdTHBoXWdIqcIk_ASWGEynl_I9chuDA5WA";

        Order order = orderRepository.save(OrderTestFactory.buildOrder(userId));
        List<OrderProduct> orderProducts = orderProductRepository.saveAll(OrderTestFactory.buildOrderProducts(order));
        createProducts();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        mockMvc.perform(get("/order/get-current-order").headers(headers))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(String.format(getFileContent("integration/order.json"),
                        order.getId()), true));
    }

    @DisplayName("Оформление заказа пользователя")
    @Test
    void completeOrder() throws Exception {
        long userId = 1;
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI0NWQwN2E2YzYzZmU0Y2EwYjgxZmU1NzhkNTQ1ZWJkYiIsInN1YiI6Iml2YW5vdmEuYUB5YW5kZXgucnUiLCJpYXQiOjE3MTAwMTg1MDgsImV4cCI6MTc0MTU1NDUwOH0.jevXRK5k0sFz1Dcalj_tigqsusLvMkmII4JpG9_zLEPdZZZYPECBtdTHBoXWdIqcIk_ASWGEynl_I9chuDA5WA";

        Order order = orderRepository.save(OrderTestFactory.buildOrder(userId));
        List<OrderProduct> orderProducts = orderProductRepository.saveAll(OrderTestFactory.buildOrderProducts(order));
        createProducts();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        mockMvc.perform(post("/order/complete-order/" + order.getId()).headers(headers))
                .andExpect(status().is2xxSuccessful());
        assertThat(orderRepository.findById(order.getId()).get().getStatus()).isEqualTo(OrderStatus.COMPLETED);
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
