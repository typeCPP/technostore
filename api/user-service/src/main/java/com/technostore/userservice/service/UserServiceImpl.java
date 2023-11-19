package com.technostore.userservice.service;

import javax.persistence.EntityNotFoundException;

import com.technostore.userservice.dto.RegisterBean;
import com.technostore.userservice.model.User;
import com.technostore.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}
