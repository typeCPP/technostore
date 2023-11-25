package com.technostore.productservice.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.technostore.productservice.dto.ProductDto;
import com.technostore.productservice.dto.SearchProductDto;
import com.technostore.productservice.dto.SortType;
import com.technostore.productservice.model.Product;
import com.technostore.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Override
    public ProductDto getProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new EntityNotFoundException("No product with such id");
        }
        Product product = productOptional.get();
        return ProductDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .category(product.getCategory())
                .description(product.getDescription())
                .name(product.getName())
                .linkPhoto(product.getLinkPhoto())
                .userRating(0.0)
                .rating(0.0)
                .reviews(List.of())
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
