package com.technostore.userservice.service;

import com.technostore.userservice.model.User;

public interface RefreshTokenService {
    void addRefreshToken(User user, String newToken);

    Long getExpirationTime();
}
