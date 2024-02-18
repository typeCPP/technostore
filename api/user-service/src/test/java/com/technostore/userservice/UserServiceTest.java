package com.technostore.userservice;

import com.technostore.userservice.dto.EditUserBean;
import com.technostore.userservice.dto.RegisterBean;
import com.technostore.userservice.dto.UserPageWithoutEmail;
import com.technostore.userservice.dto.UserProfile;
import com.technostore.userservice.model.User;
import com.technostore.userservice.repository.UserRepository;
import com.technostore.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityNotFoundException;

import java.util.List;

import static com.technostore.userservice.UserTestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Test
    void registerUserTest() {
        RegisterBean registerBean = buildRegisterBean();
        User user = userService.registerUser(registerBean);

        assertThat(user.getEmail()).isEqualTo(registerBean.getEmail());
        assertThat(user.getName()).isEqualTo(registerBean.getName());
        assertThat(user.getLastName()).isEqualTo(registerBean.getLastName());
        assertThat(user.getPassword()).isNotEqualTo(registerBean.getPassword());
    }

    @Test
    void deleteUserTest() {
        User user = saveTestUser();
        assertThat(userRepository.findById(user.getId()).isPresent()).isTrue();
        userService.deleteUser(user);
        assertThat(userRepository.findById(user.getId()).isPresent()).isFalse();
    }

    @Test
    void isEmailExistTest() {
        User user = saveTestUser();
        assertThat(userService.isEmailExist(user.getEmail())).isTrue();
        assertThat(userService.isEmailExist("email2")).isFalse();
    }

    @Test
    void setLinkPhotoTest() {
        User user = saveTestUser();
        final String NEW_LINK = "new link";
        userService.setLinkPhoto(NEW_LINK, user.getId());
        assertThat(userRepository.findById(user.getId()).get().getLinkPhoto()).isEqualTo(NEW_LINK);
    }

    @Test
    void getUserByIdTest() {
        User user = saveTestUser();
        User foundUser = userService.getUserById(user.getId());

        assertThat(user.getId()).isEqualTo(foundUser.getId());
        assertThat(user.getEmail()).isEqualTo(foundUser.getEmail());
        assertThat(user.getName()).isEqualTo(foundUser.getName());
        assertThat(user.getLastName()).isEqualTo(foundUser.getLastName());
    }

    @Test
    void getUserByIdErrorTest() {
        User user = saveTestUser();
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> userService.getUserById(user.getId() + 700));
    }

    @Test
    void isCorrectPasswordTest() {
        final String TEST_PASSWORD = "password";
        User user = saveTestUser();
        user.setPassword(passwordEncoder.encode(TEST_PASSWORD));

        assertThat(userService.isCorrectPassword(user, TEST_PASSWORD)).isTrue();
        assertThat(userService.isCorrectPassword(user, TEST_PASSWORD + "wrong")).isFalse();
    }

    @Test
    void getInfoForProfileTest() {
        User user = saveTestUser();
        UserProfile userProfile = userService.getInfoForProfile(user);

        assertThat(userProfile.getEmail()).isEqualTo(user.getEmail());
        assertThat(userProfile.getImage()).isEqualTo("/user/image?id=" + user.getId());
        assertThat(userProfile.getName()).isEqualTo(user.getName());
        assertThat(userProfile.getLastName()).isEqualTo(user.getLastName());
    }

    @Test
    void getInfoForUserPageWithoutEmailTest() {
        User user = saveTestUser();
        UserPageWithoutEmail userPageWithoutEmail = userService.getInfoForUserPageWithoutEmail(user);

        assertThat(userPageWithoutEmail.getImage()).isEqualTo("/user/image?id=" + user.getId());
        assertThat(userPageWithoutEmail.getName()).isEqualTo(user.getName());
        assertThat(userPageWithoutEmail.getLastname()).isEqualTo(user.getLastName());
    }

    @Test
    void saveTest() {
        User user = buildUser();
        userService.save(user);
    }

    @Test
    void findUserByEmailTest() {
        User user = saveTestUser();
        User foundUser = userService.findUserByEmail(user.getEmail());

        assertThat(user.getEmail()).isEqualTo(foundUser.getEmail());
        assertThat(user.getPassword()).isEqualTo(foundUser.getPassword());
        assertThat(user.getName()).isEqualTo(foundUser.getName());
        assertThat(user.getLastName()).isEqualTo(foundUser.getLastName());
    }

    @Test
    void findUserByEmailErrorTest() {
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> userService.findUserByEmail("non existing email"));
    }

    @Test
    void updateUserTest() {
        EditUserBean editUserBean = new EditUserBean("new name", "new last name");
        User user = saveTestUser();
        userService.update(user, editUserBean);

        User foundUser = userRepository.findById(user.getId()).get();

        assertThat(foundUser.getName()).isEqualTo(editUserBean.getName());
        assertThat(foundUser.getLastName()).isEqualTo(editUserBean.getLastname());
    }

    @Test
    void deleteAllUsersExceptVerifiedTest() {
        saveUnverifiedTestUser("email1");
        saveUnverifiedTestUser("email1");

        List<User> userList = userRepository.findUsersByEmail("email1");
        assertThat(userList.isEmpty()).isFalse();

        userService.deleteAllUsersExceptVerified("email1");
        userList = userRepository.findUsersByEmail("email1");
        assertThat(userList.isEmpty()).isTrue();
    }

    @Test
    void changePasswordTest() {
        User user = saveTestUser();
        String oldPassword = user.getPassword();
        userService.changePassword("new password", user);

        User foundUser = userRepository.findById(user.getId()).get();
        assertThat(foundUser.getPassword()).isNotEqualTo(oldPassword);
    }

    private User saveTestUser() {
        return userRepository.save(buildUser());
    }

    void saveUnverifiedTestUser(String email) {
        userRepository.save(buildUnverifiedUser(email));
    }
}
