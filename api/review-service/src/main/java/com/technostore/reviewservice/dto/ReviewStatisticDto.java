package com.technostore.reviewservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewStatisticDto {
    private Long productId;
    private Double rating;
    private Long countReviews;
}
