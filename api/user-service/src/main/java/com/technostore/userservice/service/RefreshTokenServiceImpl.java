package com.technostore.userservice.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.technostore.userservice.model.RefreshToken;
import com.technostore.userservice.model.User;
import com.technostore.userservice.repository.RefreshTokenRepository;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    final public int REFRESH_TOKEN_EXPIRATION_TIME_MONTH = 6;

    @Override
    public void addRefreshToken(User user, String newToken) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(newToken);
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Long getExpirationTime() {
        Calendar today = Calendar.getInstance();
        Calendar sixMonthsAhead = Calendar.getInstance();
        sixMonthsAhead.add(Calendar.MONTH, REFRESH_TOKEN_EXPIRATION_TIME_MONTH);
        return sixMonthsAhead.getTimeInMillis() - today.getTimeInMillis() - 30000;
    }
}
