package com.technostore.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class InCartCountProductDto {
    Long productId;
    Integer inCartCount;
}
