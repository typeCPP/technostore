package com.technostore.userservice.dto;

import com.technostore.userservice.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPageWithoutEmail {
    private String name;
    private String lastname;
    private String image;

    public UserPageWithoutEmail(User user) {
        this.name = user.getName();
        this.lastname = user.getLastName();
    }
}
