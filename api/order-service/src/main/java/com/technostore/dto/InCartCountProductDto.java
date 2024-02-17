package com.technostore.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class InCartCountProductDto {
    Long productId;
    Integer inCartCount;
}
