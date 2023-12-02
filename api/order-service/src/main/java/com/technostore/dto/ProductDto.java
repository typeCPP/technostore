package com.technostore.dto;

import lombok.*;

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
    private int count;
}
