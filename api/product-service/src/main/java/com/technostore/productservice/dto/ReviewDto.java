package com.technostore.productservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {
    private long id;
    private long productId;
    private String text;
    private int rate;
    private long date;
    private String userName;
    private String photoLink;
}
