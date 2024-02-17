package com.technostore;

import com.technostore.dto.OrderStatus;
import com.technostore.model.Order;
import com.technostore.model.OrderProduct;
import com.technostore.repository.OrderProductRepository;
import com.technostore.repository.OrderRepository;
import com.technostore.service.client.ProductRestTemplateClient;
import com.technostore.service.client.UserRestTemplateClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static com.technostore.OrderTestFactory.*;
import static com.technostore.TestUtils.getFileContent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderProductRepository orderProductRepository;
    @MockBean
    ProductRestTemplateClient productRestTemplateClient;
    @MockBean
    UserRestTemplateClient userRestTemplateClient;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("DELETE FROM orders.order_product;");
        jdbcTemplate.execute("DELETE FROM orders.orders;");
    }

    @Test
    void getCurrentOrderTest() throws Exception {
        Order order = orderRepository.save(buildOrder());
        OrderProduct orderProduct = orderProductRepository.save(buildOrderProduct(order));
        mockUserService(userRestTemplateClient);
        mockProductService(productRestTemplateClient, orderProduct.getProductId());

        mockMvc.perform(get("/order/get-current-order"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(String.format(getFileContent("controller/order/order.json"),
                        order.getId()), true));
    }

    @Test
    void tryGetCurrentOrderWhenLostConnectionWithUserServiceTest() throws Exception {
        when(userRestTemplateClient.getUserId(any())).thenThrow(IllegalStateException.class);
        mockMvc.perform(get("/order/get-current-order"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message", is("Lost connection with user service")));
    }

    @Test
    void completeOrderTest() throws Exception {
        Order order = orderRepository.save(buildOrder());
        orderProductRepository.save(buildOrderProduct(order));
        mockUserService(userRestTemplateClient);

        mockMvc.perform(post("/order/complete-order/" + order.getId()))
                .andExpect(status().is2xxSuccessful());

        assertThat(orderRepository.findOrderByIdAndStatus(order.getId(), OrderStatus.COMPLETED)).isPresent();
    }

    @Test
    void tryCompleteNotExistingOrderTest() throws Exception {
        mockUserService(userRestTemplateClient);

        mockMvc.perform(post("/order/complete-order/" + 10000000))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("No order with id = 10000000")));
    }

    @Test
    void tryCompleteOrderWhenLostConnectionWithUserServiceTest() throws Exception {
        when(userRestTemplateClient.getUserId(any())).thenThrow(IllegalStateException.class);
        mockMvc.perform(post("/order/complete-order/" + 10000000))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message", is("Lost connection with user service")));
    }

    @Test
    void getCompletedOrdersTest() throws Exception {
        Order order1 = orderRepository.save(buildOrder());
        Order order2 = orderRepository.save(buildOrder());
        order1.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order1);
        mockUserService(userRestTemplateClient);

        mockMvc.perform(get("/order/get-completed-orders"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(String.format(getFileContent("controller/order/completed-orders.json"),
                        order1.getId()), true));
    }

    @Test
    void tryGetCompletedOrdersWhenLostConnectionWithUserServiceTest() throws Exception {
        when(userRestTemplateClient.getUserId(any())).thenThrow(IllegalStateException.class);
        mockMvc.perform(get("/order/get-completed-orders"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message", is("Lost connection with user service")));
    }

    @Test
    void getCompletedOrderByIdTest() throws Exception {
        Order order = orderRepository.save(buildOrder());
        OrderProduct orderProduct = orderProductRepository.save(buildOrderProduct(order));
        Order order2 = orderRepository.save(buildOrder());
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
        mockUserService(userRestTemplateClient);
        mockProductService(productRestTemplateClient, orderProduct.getProductId());

        mockMvc.perform(get("/order/get-completed-order/" + order.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(String.format(getFileContent("controller/order/order.json"),
                        order.getId()), true));
    }

    @Test
    void tryGetCompletedOrderByIdWhenLostConnectionWithUserServiceTest() throws Exception {
        when(userRestTemplateClient.getUserId(any())).thenThrow(IllegalStateException.class);
        mockMvc.perform(get("/order/get-completed-order/" + 10000))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message", is("Lost connection with user service")));
    }

    @Test
    void setProductCountTest() throws Exception {
        Order order = orderRepository.save(buildOrder());
        orderProductRepository.save(buildOrderProduct(order));
        mockUserService(userRestTemplateClient);

        mockMvc.perform(post("/order/set-product-count?productId=1&count=10"))
                .andExpect(status().is2xxSuccessful());

        assertThat(orderProductRepository.findByOrder(order).get(0).getCount()).isEqualTo(10);
    }

    @Test
    void trySetProductCountWhenLostConnectionWithUserServiceTest() throws Exception {
        when(userRestTemplateClient.getUserId(any())).thenThrow(IllegalStateException.class);
        mockMvc.perform(post("/order/set-product-count?productId=1&count=10"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message", is("Lost connection with user service")));
    }
}
