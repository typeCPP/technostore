package com.technostore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private long id;
    private long productId;
    private String text;
    private int rate;
    private long date;
    private String userName;
    private String photoLink;
}
