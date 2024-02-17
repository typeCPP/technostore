package com.technostore.reviewservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ReviewDto {
    private Long id;
    private Long productId;
    private String text;
    private int rate;
    private Long date;
    private String userName;
    private String photoLink;

}
