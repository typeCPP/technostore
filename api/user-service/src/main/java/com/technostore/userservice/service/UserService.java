package com.technostore.userservice.service;

import com.technostore.userservice.dto.EditUserBean;
import com.technostore.userservice.dto.RegisterBean;
import com.technostore.userservice.dto.UserProfile;
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

    void update(User user, EditUserBean editUserBean);

    void deleteAllUsersExceptVerified(String email);

    UserProfile getInfoForProfile(User user);

    void changePassword(String password, User user);
}
