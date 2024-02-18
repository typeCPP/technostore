package com.technostore.userservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailAndTokensBean {
    private String email;
    private String refreshToken;
    private String accessToken;
}
