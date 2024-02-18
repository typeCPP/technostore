package com.technostore.productservice;

import com.technostore.productservice.model.Category;
import com.technostore.productservice.repository.CategoryRepository;
import com.technostore.productservice.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static com.technostore.productservice.ProductTestFactory.buildPopularCategories;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryServiceTest {
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("DELETE FROM product.category;");
    }

    @Test
    void getPopularCategoriesTest() {
        categoryRepository.saveAll(buildPopularCategories());

        List<Category> categories = categoryService.getPopularCategories();
        assertThat(categories.size()).isEqualTo(9);
        assertThat(categories.stream().map(Category::getName).toList())
                .containsExactlyInAnyOrder("Ноутбуки", "Планшеты", "Фототехника", "Смартфоны", "Наушники",
                        "Телевизоры", "Аксессуары", "Периферия", "Мониторы");
    }
}
