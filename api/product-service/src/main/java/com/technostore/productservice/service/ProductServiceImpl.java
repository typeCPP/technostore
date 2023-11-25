package com.technostore.productservice.service;

import com.technostore.productservice.dto.ProductDto;
import com.technostore.productservice.model.Product;
import com.technostore.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

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
}
