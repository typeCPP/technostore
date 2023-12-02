package com.technostore.productservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewStatisticDto {
    private Long productId;
    private Double rating;
    private Long countReviews;
}
