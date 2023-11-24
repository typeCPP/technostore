package com.technostore.userservice.service;

import com.technostore.userservice.model.User;

public interface RefreshTokenService {
    void addRefreshToken(User user, String newToken);

    boolean updateRefreshToken(User user, String newToken, String oldToken);

    boolean isCorrectRefreshToken(User user, String token);

    Long getExpirationTime();

    void deleteRefreshToken(User user, String newToken);

    void deleteAllRefreshTokensByUser(User user);
}
