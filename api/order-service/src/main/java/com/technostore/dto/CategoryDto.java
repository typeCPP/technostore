package com.technostore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String name;
}
