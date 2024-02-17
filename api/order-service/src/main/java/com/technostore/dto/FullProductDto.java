package com.technostore.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
public class FullProductDto {
    private long id;
    private String linkPhoto;
    private double price;
    private String name;
    private double rating;
    private double userRating;
    private String description;
    private CategoryDto category;
    private List<ReviewDto> reviews;
    private Integer inCartCount;
}
