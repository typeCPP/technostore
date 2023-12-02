package com.technostore.productservice.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import com.technostore.productservice.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.technostore.productservice.model.Product;
import com.technostore.productservice.repository.ProductRepository;
import com.technostore.productservice.service.client.ReviewRestTemplateClient;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ReviewRestTemplateClient reviewRestTemplateClient;

    @Override
    public ProductDto getProductById(Long id, HttpServletRequest request) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new EntityNotFoundException("No product with such id");
        }
        Product product = productOptional.get();
        List<ReviewDto> reviews = reviewRestTemplateClient.getAllReviews(id, request);
        double userRating = reviewRestTemplateClient.getReviewRatingByUserIdAndProductId(id, request);
        double productRating = reviewRestTemplateClient.getProductRating(id, request);

        return ProductDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .category(product.getCategory())
                .description(product.getDescription())
                .name(product.getName())
                .linkPhoto(product.getLinkPhoto())
                .userRating(userRating)
                .rating(productRating)
                .reviews(reviews)
                .build();
    }

    @Override
    public Page<SearchProductDto> searchProducts(int numberPage, int sizePage, SortType sort, String word,
                                                 Integer minRating, Integer maxRating, Integer minPrice,
                                                 Integer maxPrice, List<Long> categories, Long userId) {
        Page<SearchProductDto> page;
        if (sort == SortType.NOTHING) {
            page = productRepository.searchProducts(minPrice, maxPrice, word, categories,
                    categories.size(), PageRequest.of(numberPage, sizePage));
            List<ReviewStatisticDto> reviewStatisticDtoList =
                    reviewRestTemplateClient.getReviewStatisticsByProductIds(
                            page.getContent().stream().map(SearchProductDto::getId).toList());
            System.out.println(reviewStatisticDtoList.get(0).getRating());
            Map<Long, Double> productIdToRating = reviewStatisticDtoList.stream().collect(Collectors.toMap(
                    ReviewStatisticDto::getProductId, r -> Double.isNaN(r.getRating()) ? 0 : r.getRating()
            ));
            Map<Long, Long> productIdToCountReviews = reviewStatisticDtoList.stream().collect(Collectors.toMap(
                    ReviewStatisticDto::getProductId, ReviewStatisticDto::getCountReviews
            ));
            System.out.println(productIdToRating);
            page.getContent().forEach(r -> {
                r.setRating(productIdToRating.getOrDefault(r.getId(), 0.0));
                r.setReviewCount(productIdToCountReviews.getOrDefault(r.getId(), 0L));
            });
        } else if (sort == SortType.BY_RATING) {
            // добавить логику сортировки по рейтингу
            page = null;
        } else {
            // добавить логику сортировки по популярности
            page = null;
        }
        return page;
    }
}
