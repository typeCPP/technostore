package com.technostore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private long id;
    private String linkPhoto;
    private double price;
    private String name;
    private double rating;
    private int count;
}
