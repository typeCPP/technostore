package com.technostore.productservice;

import com.technostore.productservice.dto.InCartCountProductDto;
import com.technostore.productservice.dto.ReviewStatisticDto;
import com.technostore.productservice.model.Category;
import com.technostore.productservice.model.Product;
import com.technostore.productservice.service.client.OrderRestTemplateClient;
import com.technostore.productservice.service.client.ReviewRestTemplateClient;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class ProductTestFactory {

    public static Category buildCategory() {
        return Category.builder()
                .name("some category")
                .build();
    }

    public static List<Category> buildPopularCategories() {
        return List.of(
                Category.builder().name("Ноутбуки").build(),
                Category.builder().name("Планшеты").build(),
                Category.builder().name("Фототехника").build(),
                Category.builder().name("Смартфоны").build(),
                Category.builder().name("Наушники").build(),
                Category.builder().name("Телевизоры").build(),
                Category.builder().name("Аксессуары").build(),
                Category.builder().name("Периферия").build(),
                Category.builder().name("Мониторы").build()
        );
    }

    public static Product buildProduct(Category category) {
        return Product.builder()
                .name("some name")
                .price(15000)
                .category(category)
                .description("desc")
                .linkPhoto("photo Link")
                .build();
    }

    public static Product buildProduct(Category category, String name, double price) {
        return Product.builder()
                .name(name)
                .price(price)
                .category(category)
                .description("desc")
                .linkPhoto("photo Link")
                .build();
    }

    public static List<Product> buildProducts(Category category) {
        Product product1 = buildProduct(category, "name1", 10000);
        Product product2 = buildProduct(category, "name2", 15000);

        return List.of(product1, product2);
    }


    public static void mockOrderAndReviewService(
            ReviewRestTemplateClient reviewRestTemplateClient,
            OrderRestTemplateClient orderRestTemplateClient,
            List<Product> products) {
        Mockito.when(reviewRestTemplateClient.getReviewStatisticsByProductIds(
                        eq(List.of(products.get(0).getId(), products.get(1).getId()))))
                .thenReturn(List.of(
                        ReviewStatisticDto.builder()
                                .productId(products.get(0).getId())
                                .rating(5.0)
                                .countReviews(1L)
                                .build(),
                        ReviewStatisticDto.builder()
                                .productId(products.get(1).getId())
                                .rating(6.0)
                                .countReviews(1L)
                                .build()));

        Mockito.when(orderRestTemplateClient.getInCartCountByProductIds(
                        eq(List.of(products.get(0).getId(), products.get(1).getId())), any()))
                .thenReturn(buildListOfInCartCountProductDto(products.stream().map(Product::getId).toList()));
    }

    public static List<InCartCountProductDto> buildListOfInCartCountProductDto(List<Long> productIds) {
        return List.of(
                InCartCountProductDto.builder()
                        .productId(productIds.get(0))
                        .inCartCount(3)
                        .build(),
                InCartCountProductDto.builder()
                        .productId(productIds.get(1))
                        .inCartCount(4)
                        .build());
    }
}
