package com.technostore.userservice.service;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

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
    public boolean isCorrectRefreshToken(User user, String token) {
        List<RefreshToken> list = refreshTokenRepository.findAllByUser(user);
        for (RefreshToken refreshToken : list) {
            if (refreshToken.getToken().equals(token)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Long getExpirationTime() {
        Calendar today = Calendar.getInstance();
        Calendar sixMonthsAhead = Calendar.getInstance();
        sixMonthsAhead.add(Calendar.MONTH, REFRESH_TOKEN_EXPIRATION_TIME_MONTH);
        return sixMonthsAhead.getTimeInMillis() - today.getTimeInMillis() - 30000;
    }

    @Override
    public boolean updateRefreshToken(User user, String newToken, String oldToken) {
        Set<RefreshToken> tokenSet = user.getRefreshTokens();
        for (RefreshToken refreshToken : tokenSet) {
            if (refreshToken.getToken().equals(oldToken)) {
                RefreshToken token = new RefreshToken();
                token.setToken(newToken);
                token.setUser(user);
                refreshTokenRepository.deleteRefreshTokensByTokenAndUser(oldToken, user.getId());
                refreshTokenRepository.save(token);
                return true;
            }
        }
        return false;
    }
}
