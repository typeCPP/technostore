package com.technostore.userservice;

import com.technostore.userservice.dto.RegisterBean;
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

    public static User buildUnverifiedUser(String email) {
        return User.builder()
                .email(email)
                .password("pass")
                .name("some name")
                .lastName("last name")
                .linkPhoto("link.com")
                .isEnabled(false)
                .build();
    }

    public static RegisterBean buildRegisterBean() {
        return new RegisterBean("email", "password", "name", "lastName");
    }
}
