package com.technostore.userservice.service;

import com.technostore.userservice.model.User;

public interface UserTokenService {
    void addAccessToken(User user, String newToken);

    boolean updateAccessToken(User user, String newToken, String oldToken);

    boolean isCorrectAccessToken(User user, String token);

    Long getExpirationTime();

    void deleteAllAccessTokensByUser(User user);
}
