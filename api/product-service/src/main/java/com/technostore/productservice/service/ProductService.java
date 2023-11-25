package com.technostore.productservice.service;

import com.technostore.productservice.dto.ProductDto;

public interface ProductService {
    ProductDto getProductById(Long id);
}
