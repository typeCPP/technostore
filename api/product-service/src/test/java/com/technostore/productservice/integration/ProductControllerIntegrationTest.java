package com.technostore.productservice.integration;

import com.technostore.productservice.model.Category;
import com.technostore.productservice.model.Product;
import com.technostore.productservice.repository.CategoryRepository;
import com.technostore.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

import static com.technostore.productservice.ProductTestFactory.*;
import static com.technostore.productservice.ProductTestFactory.mockOrderAndReviewService;
import static com.technostore.productservice.TestUtils.getFileContent;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {
    private final static String JWT
            = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI0NWQwN2E2YzYzZmU0Y2EwYjgxZmU1NzhkNTQ1ZWJkYiIsInN1YiI6Iml2YW5vdmEuYUB5YW5kZXgucnUiLCJpYXQiOjE3MTAwMTg1MDgsImV4cCI6MTc0MTU1NDUwOH0.jevXRK5k0sFz1Dcalj_tigqsusLvMkmII4JpG9_zLEPdZZZYPECBtdTHBoXWdIqcIk_ASWGEynl_I9chuDA5WA";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("DELETE FROM product.product_rating;");
        jdbcTemplate.execute("DELETE FROM product.product_popularity;");
        jdbcTemplate.execute("DELETE FROM product.product;");
        jdbcTemplate.execute("DELETE FROM product.category;");
    }

    @DisplayName("Получение карточки товара со всей нужной информацией")
    @Test
    public void getProductByIdTest() throws Exception {
        Category category = categoryRepository.save(buildCategory());
        Product product = productRepository.save(buildProduct(category));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + JWT);

        mockMvc.perform(get("/product/" + product.getId()).headers(headers))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(
                        String.format(getFileContent("integration/get-product-by-id.json"),
                                product.getId(), product.getId(), category.getId()),
                        true));
    }

    @DisplayName("Получение информации о нескольких товарах по id")
    @Test
    public void getProductByIdsTest() throws Exception {
        Category category = categoryRepository.save(buildCategory());
        List<Product> products = productRepository.saveAll(buildProducts(category));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + JWT);

        mockMvc.perform(get("/product/products-by-ids")
                        .param("ids", products.stream().map(p -> String.valueOf(p.getId())).collect(Collectors.joining(",")))
                        .headers(headers)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(
                        String.format(getFileContent("integration/products-by-ids.json"),
                                products.get(0).getId(), products.get(0).getId(), category.getId(),
                                products.get(1).getId(), products.get(1).getId(), category.getId()),
                        true));
    }
}
