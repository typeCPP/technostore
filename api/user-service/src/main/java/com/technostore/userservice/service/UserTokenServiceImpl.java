package com.technostore.userservice.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.technostore.userservice.model.User;
import com.technostore.userservice.model.UserToken;
import com.technostore.userservice.repository.UserTokenRepository;

@Service
public class UserTokenServiceImpl implements UserTokenService {

    @Autowired
    UserTokenRepository userTokenRepository;

    final public int ACCESS_TOKEN_EXPIRATION_TIME_MINUTES = 30;

    @Override
    public void addAccessToken(User user, String newToken) {
        UserToken userToken = new UserToken();
        userToken.setToken(newToken);
        userToken.setUser(user);
        userTokenRepository.save(userToken);
    }

    @Override
    public Long getExpirationTime() {
        Calendar today = Calendar.getInstance();
        Calendar sixMonthsAhead = Calendar.getInstance();
        sixMonthsAhead.add(Calendar.MINUTE, ACCESS_TOKEN_EXPIRATION_TIME_MINUTES);
        return sixMonthsAhead.getTimeInMillis() - today.getTimeInMillis() - 30000;
    }
}
