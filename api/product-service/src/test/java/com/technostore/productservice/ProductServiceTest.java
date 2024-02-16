package com.technostore.productservice;

import com.technostore.productservice.dto.*;
import com.technostore.productservice.model.Category;
import com.technostore.productservice.model.Product;
import com.technostore.productservice.repository.CategoryRepository;
import com.technostore.productservice.repository.ProductRepository;
import com.technostore.productservice.service.ProductService;
import com.technostore.productservice.service.client.OrderRestTemplateClient;
import com.technostore.productservice.service.client.ReviewRestTemplateClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void getProductByIdTest() {
        Category category = categoryRepository.save(Category.builder()
                .name("some category")
                .build());
        Product product = productRepository.save(Product.builder()
                .name("some name")
                .price(15000)
                .category(category)
                .description("desc")
                .linkPhoto("photoLink")
                .build());

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
    void getPhotoLinkTest() {
        Category category = categoryRepository.save(Category.builder()
                .name("some category")
                .build());
        Product product = productRepository.save(Product.builder()
                .name("some name")
                .price(15000)
                .category(category)
                .description("desc")
                .linkPhoto("photo Link")
                .build());
        String photoLink = productService.getPhotoLink(product.getId());

        assertThat(photoLink).isEqualTo("photo Link");

    }

    @Test
    void searchProductsTest() {
        Category category = categoryRepository.save(Category.builder()
                .name("some category")
                .build());
        Product product1 = productRepository.save(Product.builder()
                .name("name1")
                .price(10000)
                .category(category)
                .description("desc")
                .linkPhoto("photo Link")
                .build());

        Product product2 = productRepository.save(Product.builder()
                .name("name2")
                .price(15000)
                .category(category)
                .description("desc")
                .linkPhoto("photo Link")
                .build());

        Mockito.when(reviewRestTemplateClient.getReviewStatisticsByProductIds(eq(List.of(product1.getId(), product2.getId()))))
                .thenReturn(List.of(
                        ReviewStatisticDto.builder()
                                .productId(product1.getId())
                                .rating(5.0)
                                .countReviews(1L)
                                .build(),
                        ReviewStatisticDto.builder()
                                .productId(product2.getId())
                                .rating(6.0)
                                .countReviews(1L)
                                .build()));

        Mockito.when(orderRestTemplateClient.getInCartCountByProductIds(eq(List.of(product1.getId(), product2.getId())), any()))
                .thenReturn(List.of(
                        InCartCountProductDto.builder()
                                .productId(product1.getId())
                                .inCartCount(3)
                                .build(),
                        InCartCountProductDto.builder()
                                .productId(product2.getId())
                                .inCartCount(4)
                                .build()));

        List<SearchProductDto> productDtoList = productService.searchProducts(0, 2, SortType.BY_POPULARITY,
                "name", 0, 10, 0, 20000, List.of(category.getId()), null).get().toList();
        assertThat(productDtoList.size()).isEqualTo(2);
        assertThat(productDtoList.get(0).getName()).isEqualTo(product1.getName());
        assertThat(productDtoList.get(0).getPrice()).isEqualTo(product1.getPrice());
        assertThat(productDtoList.get(1).getName()).isEqualTo(product2.getName());
        assertThat(productDtoList.get(1).getPrice()).isEqualTo(product2.getPrice());

        productDtoList = productService.searchProducts(0, 2, SortType.BY_POPULARITY,
                "name", 0, 10, 0, 14000, List.of(category.getId()), null).get().toList();
        assertThat(productDtoList.size()).isEqualTo(1);
        assertThat(productDtoList.get(0).getName()).isEqualTo(product1.getName());
        assertThat(productDtoList.get(0).getPrice()).isEqualTo(product1.getPrice());

        productDtoList = productService.searchProducts(0, 2, SortType.BY_POPULARITY,
                "name", 0, 10, 11000, 20000, List.of(category.getId()), null).get().toList();
        assertThat(productDtoList.size()).isEqualTo(1);
        assertThat(productDtoList.get(0).getName()).isEqualTo(product2.getName());
        assertThat(productDtoList.get(0).getPrice()).isEqualTo(product2.getPrice());

        productDtoList = productService.searchProducts(0, 2, SortType.BY_POPULARITY,
                "name", 0, 10, 20000, 20001, List.of(category.getId()), null).get().toList();
        assertThat(productDtoList.size()).isEqualTo(0);

        productDtoList = productService.searchProducts(0, 2, SortType.BY_POPULARITY,
                "word", 0, 10, 0, 20000, List.of(category.getId()), null).get().toList();
        assertThat(productDtoList.size()).isEqualTo(0);
    }
}
