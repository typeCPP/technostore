package com.technostore.userservice.service;

import com.technostore.userservice.model.User;

public interface UserTokenService {
    void addAccessToken(User user, String newToken);

    Long getExpirationTime();
}
