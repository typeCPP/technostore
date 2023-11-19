package com.technostore.userservice.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import com.technostore.userservice.dto.RegisterBean;
import com.technostore.userservice.model.User;
import com.technostore.userservice.security.jwt.JwtService;
import com.technostore.userservice.service.MailService;
import com.technostore.userservice.service.RefreshTokenService;
import com.technostore.userservice.service.UserService;
import com.technostore.userservice.service.UserTokenService;
import com.technostore.userservice.utils.AppError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/user")
public class UserController {

    private final int ACCESS_TOKEN_EXPIRATION_TIME_MINUTES = 30;

    private final int REFRESH_TOKEN_EXPIRATION_TIME_MONTH = 6;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Autowired
    public BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @RequestMapping(path = "/registration", method = RequestMethod.POST, consumes = {"multipart/form-data"}, produces = "application/json")
    public ResponseEntity<?> register(@RequestPart String registerBeanString, @RequestPart(value = "file", required = false) MultipartFile file) {
        ObjectMapper mapper = new ObjectMapper();
        RegisterBean registerBean;
        try {
            registerBean = mapper.readValue(registerBeanString, RegisterBean.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Failed to convert JSON object"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User user;
        if (userService.isEmailExist(registerBean.getEmail())) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.CONFLICT.value(),
                            "This email is already in use"), HttpStatus.CONFLICT);
        }

        user = userService.registerUser(registerBean);
        try {
            if (file != null) {
                String path = new File("").getAbsolutePath();
                File newFile = new File(path + user.getId() + ".0.jpg");
                file.transferTo(newFile);
                userService.setLinkPhoto(user.getId() + ".0.jpg", user.getId());
            } else {
                userService.setLinkPhoto(null, user.getId());
            }
        } catch (IOException e) {
            userService.deleteUser(user);
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Failed to convert photo"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            mailService.sendConfirmationCode(user.getId());
        } catch (MessagingException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error sending code."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            Map<String, String> map = generateMapWithInfoAboutTokens(user);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (IOException | URISyntaxException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error generation token!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, String> generateMapWithInfoAboutTokens(User user) throws IOException, URISyntaxException {
        String newAccessToken = jwtService.accessTokenFor(user.getEmail(), ACCESS_TOKEN_EXPIRATION_TIME_MINUTES);
        userTokenService.addAccessToken(user, newAccessToken);
        String newRefreshToken = jwtService.refreshTokenFor(user.getEmail(), REFRESH_TOKEN_EXPIRATION_TIME_MONTH);
        refreshTokenService.addRefreshToken(user, newRefreshToken);

        Map<String, String> map = new HashMap<>();
        map.put("access-token", newAccessToken);
        map.put("expireTimeAccessToken", String.valueOf(userTokenService.getExpirationTime()));
        map.put("refresh-token", newRefreshToken);
        map.put("expireTimeRefreshToken", String.valueOf(refreshTokenService.getExpirationTime()));
        map.put("id", String.valueOf(user.getId()));
        map.put("email", user.getEmail());
        return map;
    }
}