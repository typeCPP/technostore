package com.technostore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String name;
}
