package com.technostore.productservice;

import com.technostore.productservice.model.Category;
import com.technostore.productservice.model.Product;
import com.technostore.productservice.repository.CategoryRepository;
import com.technostore.productservice.repository.ProductRepository;
import com.technostore.productservice.service.client.OrderRestTemplateClient;
import com.technostore.productservice.service.client.ReviewRestTemplateClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.technostore.productservice.ProductTestFactory.*;
import static com.technostore.productservice.TestUtils.getFileContent;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @MockBean
    ReviewRestTemplateClient reviewRestTemplateClient;
    @MockBean
    OrderRestTemplateClient orderRestTemplateClient;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("TRUNCATE TABLE product.product_rating;");
        jdbcTemplate.execute("TRUNCATE TABLE product.product_popularity;");
        jdbcTemplate.execute("TRUNCATE TABLE product.product;");
        jdbcTemplate.execute("TRUNCATE TABLE product.category;");
    }

    @Test
    public void getProductByIdTest() throws Exception {
        Category category = categoryRepository.save(buildCategory());
        Product product = productRepository.save(buildProduct(category));

        mockMvc.perform(get("/product/" + product.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(getFileContent("controller/product/get-product-by-id.json"), true));
    }

    @Test
    public void searchProductsTest() throws Exception {
        Category category = categoryRepository.save(buildCategory());
        List<Product> products = productRepository.saveAll(buildProducts(category));
        mockOrderAndReviewService(reviewRestTemplateClient, orderRestTemplateClient, products);

        mockMvc.perform(get("/product/search").param("numberPage", "0").param("sizePage", "2"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(getFileContent("controller/product/search-products.json"), true));
    }

    @Test
    public void getPopularCategoriesTest() throws Exception {
        categoryRepository.saveAll(buildPopularCategories());

        mockMvc.perform(get("/product/popular-categories"))
                .andExpect(status().is2xxSuccessful());
    }
}