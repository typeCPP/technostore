package com.technostore.userservice.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technostore.userservice.dto.EditUserBean;
import com.technostore.userservice.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.technostore.userservice.model.User;
import com.technostore.userservice.security.jwt.JwtService;
import com.technostore.userservice.service.UserService;
import com.technostore.userservice.service.UserTokenService;
import com.technostore.userservice.utils.AppError;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class AuthUserController {

    private final static String IMAGE_PATH = "/app/images/";

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

    @RequestMapping(path = "/check-password", method = RequestMethod.GET)
    public ResponseEntity<?> checkPassword(@RequestParam String password, HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        Map<String, Boolean> map = new HashMap<>();
        map.put("isCorrectPassword", userService.isCorrectPassword(user, password));
        return new ResponseEntity<>(map, HttpStatus.OK);
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

    @RequestMapping(path = "/edit-profile", method = RequestMethod.POST, consumes = {"multipart/form-data"}, produces = "application/json")
    public ResponseEntity<?> editProfile(@RequestPart String editUserBeanString,
                                         @RequestPart(required = false) MultipartFile file,
                                         HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        ObjectMapper mapper = new ObjectMapper();
        EditUserBean editUserBean;
        try {
            editUserBean = mapper.readValue(editUserBeanString, EditUserBean.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Failed to convert JSON object"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        int photoVersion = 0;
        try {
            if (file != null) {
                String oldLink = user.getLinkPhoto();
                if (oldLink != null) {
                    Scanner sc = new Scanner(oldLink);
                    sc.useDelimiter("\\.");
                    if (sc.hasNextInt()) {
                        sc.nextInt();
                        photoVersion = sc.nextInt();
                    }
                    photoVersion++;
                }
                String path = new File("").getAbsolutePath();
                File newFile = new File(path + IMAGE_PATH + user.getId() + "." + photoVersion + ".jpg");
                file.transferTo(newFile);
                File oldPhoto = new File(path + IMAGE_PATH + user.getId() + "." + --photoVersion + ".jpg");
                oldPhoto.delete();
            }
        } catch (IOException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Failed to convert photo"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        userService.update(user, editUserBean);
        if (file != null) {
            userService.setLinkPhoto(IMAGE_PATH + user.getId() + "." + ++photoVersion + ".jpg", user.getId());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public ResponseEntity<?> logout(@RequestParam String refreshToken,
                                    HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        User user = (User) responseEntity.getBody();
        userTokenService.deleteAccessToken(user, resolveToken(request));
        refreshTokenService.deleteRefreshToken(user, refreshToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/get-user-id", method = RequestMethod.GET)
    public ResponseEntity<?> getUserId(HttpServletRequest request) {
        ResponseEntity<?> responseEntity = getUserByRequest(request);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity;
        }
        return new ResponseEntity<>(((User) responseEntity.getBody()).getId(), HttpStatus.OK);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
