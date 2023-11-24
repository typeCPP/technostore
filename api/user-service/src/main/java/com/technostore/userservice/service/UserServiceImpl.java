package com.technostore.userservice.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.technostore.userservice.dto.RegisterBean;
import com.technostore.userservice.dto.UserProfile;
import com.technostore.userservice.model.User;
import com.technostore.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    public BCryptPasswordEncoder passwordEncoder;


    @Override
    public User registerUser(RegisterBean register) {
        User newUser = new User();
        newUser.setLastName(register.getLastName());
        newUser.setName(register.getName());
        newUser.setEmail(register.getEmail());
        newUser.setPassword(passwordEncoder.encode(register.getPassword()));
        return userRepository.save(newUser);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.deleteById(user.getId());
    }

    @Override
    public boolean isEmailExist(String email) {
        List<User> users = userRepository.findUsersByEmail(email);
        for (User user : users) {
            if (user.isEnabled()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setLinkPhoto(String linkPhoto, Long userId) {
        userRepository.updateLinkPhotoByUserId(linkPhoto, userId);
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        throw new EntityNotFoundException("User not found.");
    }

    @Override
    public boolean isCorrectPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public User findUserByEmail(String email) {
        List<User> users = userRepository.findUsersByEmail(email);
        for (User u : users) {
            if (u.isEnabled()) {
                return u;
            }
        }
        throw new EntityNotFoundException("User not found.");
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteAllUsersExceptVerified(String email) {
        userRepository.deleteAllUsersExceptVerified(email);
    }

    @Override
    public UserProfile getInfoForProfile(User user) {
        UserProfile userProfile = new UserProfile(user);
        userProfile.setImage("/user/image?id=" + user.getId());
        return userProfile;
    }
}
