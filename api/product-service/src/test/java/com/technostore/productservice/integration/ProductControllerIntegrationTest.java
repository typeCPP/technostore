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
import static com.technostore.productservice.TestUtils.getFileContent;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .andExpect(jsonPath("$[0].linkPhoto").value("/product/image?id=" + products.get(0).getId()))
                .andExpect(jsonPath("$[0].price").value(10000.0))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[0].rating").value(5.0))
                .andExpect(jsonPath("$[0].userRating").value(0.0))
                .andExpect(jsonPath("$[0].description").value("desc"))
                .andExpect(jsonPath("$[0].category.id").value(category.getId()))
                .andExpect(jsonPath("$[0].category.name").value("some category"))
                .andExpect(jsonPath("$[0].reviews", hasSize(2)))
                .andExpect(jsonPath("$[1].linkPhoto").value("/product/image?id=" + products.get(1).getId()))
                .andExpect(jsonPath("$[1].price").value(15000.0))
                .andExpect(jsonPath("$[1].name").value("name2"))
                .andExpect(jsonPath("$[1].rating").value(10.0))
                .andExpect(jsonPath("$[1].userRating").value(0.0))
                .andExpect(jsonPath("$[1].description").value("desc"))
                .andExpect(jsonPath("$[1].category.id").value(category.getId()))
                .andExpect(jsonPath("$[1].category.name").value("some category"))
                .andExpect(jsonPath("$[1].reviews", hasSize(1)))
                .andExpect(jsonPath("$[1].inCartCount").value(0));
    }

    @DisplayName("Поиск товаров")
    @Test
    public void searchProductsTest() throws Exception {
        Category category = categoryRepository.save(buildCategory());
        List<Product> products = productRepository.saveAll(buildProducts(category));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + JWT);

        mockMvc.perform(get("/product/search")
                        .param("numberPage", "0")
                        .param("sizePage", "2")
                        .param("word", "n")
                        .headers(headers)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(
                        String.format(getFileContent("integration/search-products.json"),
                                1, 1, 2, 2),
                        true));
    }
}
