package com.technostore.userservice.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import com.technostore.userservice.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.technostore.userservice.model.User;
import com.technostore.userservice.security.jwt.JwtService;
import com.technostore.userservice.service.UserService;
import com.technostore.userservice.service.UserTokenService;
import com.technostore.userservice.utils.AppError;

@RestController
@RequestMapping("/user")
public class AuthUserController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @RequestMapping(path = "/profile", method = RequestMethod.GET)
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        return new ResponseEntity<>(userService.getInfoForProfile(user), HttpStatus.OK);
    }

    private ResponseEntity<?> getUserByRequest(HttpServletRequest request) {
        String email;
        String token = resolveToken(request);
        try {
            email = jwtService.extractUserInfo(token);
        } catch (URISyntaxException | IOException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Extracting user info by token is failed."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User user;
        try {
            user = userService.findUserByEmail(email);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "User with email " + email + " does not exist."), HttpStatus.NOT_FOUND);
        }
        if (!userTokenService.isCorrectAccessToken(user, token)) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(),
                            "This access token does not exist."), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(path = "/change-password", method = RequestMethod.GET)
    public ResponseEntity<?> changePassword(@RequestParam String newPassword,
                                            @RequestParam(required = false) String oldPassword,
                                            @RequestParam String refreshToken,
                                            HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        if (oldPassword != null) {
            if (!userService.isCorrectPassword(user, oldPassword)) {
                return new ResponseEntity<>(
                        new AppError(HttpStatus.NOT_FOUND.value(),
                                "Wrong old password!"), HttpStatus.NOT_FOUND);
            }
        }
        userTokenService.deleteAllAccessTokensByUser(user);
        refreshTokenService.deleteAllRefreshTokensByUser(user);
        userTokenService.addAccessToken(user, resolveToken(request));
        refreshTokenService.addRefreshToken(user, refreshToken);
        userService.changePassword(newPassword, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/check-token", method = RequestMethod.GET)
    public ResponseEntity<?> checkToken(HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}