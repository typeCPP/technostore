package com.technostore.userservice.service;

import com.technostore.userservice.dto.RegisterBean;
import com.technostore.userservice.model.User;

public interface UserService {
    User registerUser(RegisterBean register);

    void deleteUser(User user);

    boolean isEmailExist(String email);

    void setLinkPhoto(String linkPhoto, Long userId);

    User getUserById(Long id);

    User findUserByEmail(String email);

    boolean isCorrectPassword(User user, String password);

    void save(User user);

    void deleteAllUsersExceptVerified(String email);
}
