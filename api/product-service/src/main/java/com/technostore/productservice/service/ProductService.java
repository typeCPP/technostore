package com.technostore.productservice.service;

import com.technostore.productservice.dto.ProductDto;
import com.technostore.productservice.dto.SearchProductDto;
import com.technostore.productservice.dto.SortType;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProductService {
    ProductDto getProductById(Long id, HttpServletRequest request);
    Page<SearchProductDto> searchProducts(int numberPage, int sizePage, SortType sort, String word, Integer minRating,
                                          Integer maxRating, Integer minPrice, Integer maxPrice, List<Long> categorie,
                                          Long userId);
}
