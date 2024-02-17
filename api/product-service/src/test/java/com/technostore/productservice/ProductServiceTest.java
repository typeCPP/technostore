package com.technostore.productservice;

import com.technostore.productservice.dto.*;
import com.technostore.productservice.model.Category;
import com.technostore.productservice.model.Product;
import com.technostore.productservice.repository.CategoryRepository;
import com.technostore.productservice.repository.ProductRepository;
import com.technostore.productservice.service.ProductService;
import com.technostore.productservice.service.client.OrderRestTemplateClient;
import com.technostore.productservice.service.client.ReviewRestTemplateClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static com.technostore.productservice.ProductTestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductServiceTest {
    @Autowired
    ProductService productService;
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
    void getProductByIdTest() {
        Category category = categoryRepository.save(buildCategory());
        Product product = productRepository.save(buildProduct(category));

        ReviewDto reviewDto = ReviewDto.builder()
                .id(1)
                .date(1000)
                .rate(5)
                .userName("some name")
                .productId(product.getId())
                .photoLink("some link")
                .text("text of review")
                .build();

        Mockito.when(reviewRestTemplateClient.getAllReviews(eq(product.getId()), any()))
                .thenReturn(List.of(reviewDto));

        Mockito.when(reviewRestTemplateClient.getReviewRatingByUserIdAndProductId(eq(product.getId()), any()))
                .thenReturn(5.0);

        Mockito.when(reviewRestTemplateClient.getProductRating(eq(product.getId()), any()))
                .thenReturn(5.0);

        Mockito.when(orderRestTemplateClient.getInCartCountByProductIds(eq(List.of(product.getId())), any()))
                .thenReturn(List.of(InCartCountProductDto.builder()
                        .productId(product.getId())
                        .inCartCount(20)
                        .build()));

        ProductDto productDto = productService.getProductById(product.getId(), null);

        assertThat(productDto.getId()).isEqualTo(product.getId());
        assertThat(productDto.getName()).isEqualTo(product.getName());
        assertThat(productDto.getPrice()).isEqualTo(product.getPrice());
        assertThat(productDto.getDescription()).isEqualTo(product.getDescription());
        assertThat(productDto.getRating()).isEqualTo(5.0);
        assertThat(productDto.getUserRating()).isEqualTo(5.0);
        assertThat(productDto.getInCartCount()).isEqualTo(20);
        assertThat(productDto.getReviews().size()).isEqualTo(1);
        assertThat(productDto.getReviews().get(0)).isEqualTo(reviewDto);
        assertThat(productDto.getCategory().getId()).isEqualTo(category.getId());
        assertThat(productDto.getCategory().getName()).isEqualTo(category.getName());
    }

    @Test
    void whenGetProductByIdForNotExistingProductThenThrow() {
        assertThatThrownBy(() -> productService.getProductById(100000000000L, null))
                .isExactlyInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getPhotoLinkTest() {
        Category category = categoryRepository.save(buildCategory());
        Product product = productRepository.save(buildProduct(category));

        String photoLink = productService.getPhotoLink(product.getId());

        assertThat(photoLink).isEqualTo(product.getLinkPhoto());
    }

    @Test
    void whenGetPhotoLinkForNotExistingProductThenThrow() {
        assertThatThrownBy(() -> productService.getPhotoLink(100000000000L))
                .isExactlyInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void searchProductsTest() {
        Category category = categoryRepository.save(buildCategory());
        List<Product> products = productRepository.saveAll(buildProducts(category));

        mockOrderAndReviewService(reviewRestTemplateClient, orderRestTemplateClient, products);

        List<SearchProductDto> productDtoList = productService.searchProducts(0, 2, SortType.BY_POPULARITY,
                "name", 0, 10, 0, 20000, List.of(category.getId()), null).get().toList();
        assertThat(productDtoList.size()).isEqualTo(2);
        assertThat(productDtoList.get(0).getName()).isEqualTo(products.get(0).getName());
        assertThat(productDtoList.get(1).getPrice()).isEqualTo(products.get(1).getPrice());
        assertThat(productDtoList.get(0).getName()).isEqualTo(products.get(0).getName());
        assertThat(productDtoList.get(1).getPrice()).isEqualTo(products.get(1).getPrice());
    }

    @Test
    void searchProductsByRatingTest() {
        Category category = categoryRepository.save(buildCategory());
        List<Product> products = productRepository.saveAll(buildProducts(category));
        jdbcTemplate.execute(String.format("""
                INSERT INTO product.product_rating(count_rating, sum_rating, product_id)
                VALUES (1, 5, %s),
                       (1, 6, %s)
                """, products.get(0).getId(), products.get(1).getId()));

        mockOrderAndReviewService(reviewRestTemplateClient, orderRestTemplateClient, products);

        List<SearchProductDto> productDtoList = productService.searchProducts(0, 2, SortType.BY_RATING,
                "name", 0, 10, 0, 20000, List.of(category.getId()), null).get().toList();
        assertThat(productDtoList.size()).isEqualTo(2);
        assertThat(productDtoList.get(1).getName()).isEqualTo(products.get(0).getName());
        assertThat(productDtoList.get(0).getPrice()).isEqualTo(products.get(1).getPrice());
        assertThat(productDtoList.get(1).getName()).isEqualTo(products.get(0).getName());
        assertThat(productDtoList.get(0).getPrice()).isEqualTo(products.get(1).getPrice());
    }

    @Test
    void whenSearchProductsAndUseUnexpectedWordThenReturnEmptyList() {
        Category category = categoryRepository.save(buildCategory());
        List<Product> products = productRepository.saveAll(buildProducts(category));
        mockOrderAndReviewService(reviewRestTemplateClient, orderRestTemplateClient, products);

        List<SearchProductDto> productDtoList = productService.searchProducts(0, 2, SortType.BY_POPULARITY,
                "word", 0, 10, 0, 20000, List.of(category.getId()), null).get().toList();
        assertThat(productDtoList.size()).isEqualTo(0);
    }

    @Test
    void whenSearchProductsAndPartOfProductsHavePriceMoreMaxPriceThanInQuery() {
        Category category = categoryRepository.save(buildCategory());
        List<Product> products = productRepository.saveAll(buildProducts(category));
        mockOrderAndReviewService(reviewRestTemplateClient, orderRestTemplateClient, products);

        List<SearchProductDto> productDtoList = productService.searchProducts(0, 2, SortType.BY_POPULARITY,
                "name", 0, 10, 0, 14000, List.of(category.getId()), null).get().toList();
        assertThat(productDtoList.size()).isEqualTo(1);
        assertThat(productDtoList.get(0).getName()).isEqualTo(products.get(0).getName());
        assertThat(productDtoList.get(0).getPrice()).isEqualTo(products.get(0).getPrice());
    }

    @Test
    void whenSearchProductsAndPartOfProductsHavePriceLessMinPriceThanInQuery() {
        Category category = categoryRepository.save(buildCategory());
        List<Product> products = productRepository.saveAll(buildProducts(category));
        mockOrderAndReviewService(reviewRestTemplateClient, orderRestTemplateClient, products);

        List<SearchProductDto> productDtoList = productService.searchProducts(0, 2, SortType.BY_POPULARITY,
                "name", 0, 10, 11000, 20000, List.of(category.getId()), null).get().toList();
        assertThat(productDtoList.size()).isEqualTo(1);
        assertThat(productDtoList.get(0).getName()).isEqualTo(products.get(1).getName());
        assertThat(productDtoList.get(0).getPrice()).isEqualTo(products.get(1).getPrice());
    }
}
