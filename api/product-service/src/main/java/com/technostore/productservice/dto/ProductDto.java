package com.technostore.productservice.dto;

import com.technostore.productservice.model.Category;
import lombok.*;

import java.util.List;

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
