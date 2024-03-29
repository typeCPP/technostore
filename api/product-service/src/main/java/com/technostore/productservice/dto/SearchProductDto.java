package com.technostore.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import com.technostore.productservice.model.Product;

@Getter
@Setter
@AllArgsConstructor
public class SearchProductDto {
    private long id;
    private String linkPhoto;
    private double price;
    private String name;
    private double rating;
    private Long reviewCount;
    @Nullable
    private Integer inCartCount;

    public SearchProductDto(Product product) {
        this.id = product.getId();
        this.linkPhoto = product.getLinkPhoto();
        this.price = product.getPrice();
        this.name = product.getName();
    }
}
