package com.technostore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class InCartCountProductDto {
    Long productId;
    Integer inCartCount;
}
