package com.technostore.userservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginBean {
    private String email;
    private String password;
    private String refreshToken;
    private String accessToken;
}
