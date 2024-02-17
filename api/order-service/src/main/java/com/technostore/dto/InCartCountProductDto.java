package com.technostore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class InCartCountProductDto {
    Long productId;
    Integer inCartCount;
}
