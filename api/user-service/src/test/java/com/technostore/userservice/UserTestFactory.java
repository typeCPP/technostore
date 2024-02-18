package com.technostore.userservice;

import com.technostore.userservice.model.User;

public class UserTestFactory {
    public static User buildUser() {
        return User.builder()
                .email("email")
                .password("pass")
                .name("some name")
                .lastName("last name")
                .linkPhoto("link.com")
                .isEnabled(true)
                .build();
    }
}
