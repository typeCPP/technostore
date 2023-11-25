package com.technostore.reviewservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
