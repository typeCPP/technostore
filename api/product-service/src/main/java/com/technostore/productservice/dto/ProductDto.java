package com.technostore.productservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.technostore.productservice.model.Category;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private long id;
    private String linkPhoto;
    private double price;
    private String name;
    private double rating;
    private double userRating;
    private String description;
    private Category category;
    private List<ReviewDto> reviews;
}
