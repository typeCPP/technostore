package com.technostore.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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
}
