package com.technostore.userservice;

import com.technostore.userservice.controller.UserController;
import com.technostore.userservice.model.User;
import com.technostore.userservice.repository.UserRepository;
import com.technostore.userservice.repository.UserTokenRepository;
import com.technostore.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

import static com.technostore.userservice.TestUtils.getFileContent;
import static com.technostore.userservice.UserTestFactory.buildRegisterBean;
import static com.technostore.userservice.UserTestFactory.buildUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthUserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserTokenRepository userTokenRepository;
    @Autowired
    UserController userController;
    @Autowired
    UserService userService;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("DELETE FROM user.user_jwt;");
        jdbcTemplate.execute("DELETE FROM user.user_refresh_token;");
        jdbcTemplate.execute("DELETE FROM user.verify_code;");
        jdbcTemplate.execute("DELETE FROM user.users;");
    }

    @Test
    void getProfileTest() throws Exception {
        User user = userRepository.save(buildUser());
        Map<String, String> map = userController.generateMapWithInfoAboutTokens(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + map.get("access-token"));

        mockMvc.perform(get("/user/profile").headers(headers))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(String.format(getFileContent("controller/user/profile.json"),
                        user.getId()), true));
    }

    @Test
    void getProfileWhenInvalidEmailInHeaderTest() throws Exception {
        User user = userRepository.save(buildUser());
        Map<String, String> map = userController.generateMapWithInfoAboutTokens(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + map.get("access-token"));
        userRepository.delete(user);

        mockMvc.perform(get("/user/profile").headers(headers))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("User with email email does not exist.")));
    }

    @Test
    void checkPasswordTest() throws Exception {
        User user = userRepository.save(buildUser());
        Map<String, String> map = userController.generateMapWithInfoAboutTokens(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + map.get("access-token"));

        mockMvc.perform(get("/user/check-password?password=invalidpassword").headers(headers))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json("{\"isCorrectPassword\":false}", true));
    }

    @Test
    void getUserIdTest() throws Exception {
        User user = userRepository.save(buildUser());
        Map<String, String> map = userController.generateMapWithInfoAboutTokens(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + map.get("access-token"));

        mockMvc.perform(get("/user/get-user-id").headers(headers))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(String.valueOf(user.getId()), true));
    }

    @Test
    void changePasswordTest() throws Exception {
        User user = userService.registerUser(buildRegisterBean());
        user.setEnabled(true);
        userRepository.save(user);
        Map<String, String> map = userController.generateMapWithInfoAboutTokens(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + map.get("access-token"));

        mockMvc.perform(get("/user/change-password")
                        .param("newPassword", "superpassword")
                        .param("oldPassword", "password")
                        .param("refreshToken", map.get("refresh-token"))
                        .headers(headers))
                .andExpect(status().is2xxSuccessful());
        assertThat(userRepository.findById(user.getId()).get().getPassword()).isNotEqualTo(user.getPassword());
    }

    @Test
    void tryChangePasswordWhenOldPasswordIsIncorrectTest() throws Exception {
        User user = userService.registerUser(buildRegisterBean());
        user.setEnabled(true);
        userRepository.save(user);
        Map<String, String> map = userController.generateMapWithInfoAboutTokens(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + map.get("access-token"));

        mockMvc.perform(get("/user/change-password")
                        .param("newPassword", "superpassword")
                        .param("oldPassword", "some pass")
                        .param("refreshToken", map.get("refresh-token"))
                        .headers(headers))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Wrong old password!")));
    }

    @Test
    void changePasswordWhenOldPasswordIsNullInRequestTest() throws Exception {
        User user = userService.registerUser(buildRegisterBean());
        user.setEnabled(true);
        userRepository.save(user);
        Map<String, String> map = userController.generateMapWithInfoAboutTokens(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + map.get("access-token"));

        mockMvc.perform(get("/user/change-password")
                        .param("newPassword", "superpassword")
                        .param("refreshToken", map.get("refresh-token"))
                        .headers(headers))
                .andExpect(status().is2xxSuccessful());
        assertThat(userRepository.findById(user.getId()).get().getPassword()).isNotEqualTo(user.getPassword());
    }

    @Test
    void editProfileTest() throws Exception {
        User user = userService.registerUser(buildRegisterBean());
        user.setEnabled(true);
        userRepository.save(user);
        Map<String, String> map = userController.generateMapWithInfoAboutTokens(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + map.get("access-token"));

        MockMultipartFile jsonFile = new MockMultipartFile("editUserBeanString", "", "application/json",
                getFileContent("controller/user/edit-profile-request.json").getBytes());
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/user/edit-profile")
                        .file(jsonFile)
                        .headers(headers)
                )
                .andExpect(status().is2xxSuccessful());
        assertThat(userRepository.findById(user.getId()).get().getName()).isEqualTo("Nastia");
        assertThat(userRepository.findById(user.getId()).get().getLastName()).isEqualTo("Molchanova");
    }

    @Test
    void editProfileWhenInvalidRequestTest() throws Exception {
        User user = userService.registerUser(buildRegisterBean());
        user.setEnabled(true);
        userRepository.save(user);
        Map<String, String> map = userController.generateMapWithInfoAboutTokens(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + map.get("access-token"));

        MockMultipartFile jsonFile = new MockMultipartFile("editUserBeanString", "", "application/json",
                "ejndjerndjrnknfjkrncjknrjkcnrj".getBytes());
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/user/edit-profile")
                        .file(jsonFile)
                        .headers(headers)
                )
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message", is("Failed to convert JSON object")));
    }

    @Test
    void checkTokenTest() throws Exception {
        User user = userService.registerUser(buildRegisterBean());
        user.setEnabled(true);
        userRepository.save(user);
        Map<String, String> map = userController.generateMapWithInfoAboutTokens(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + map.get("access-token"));

        mockMvc.perform(get("/user/check-token").headers(headers))
                .andExpect(status().is2xxSuccessful());
    }
}
