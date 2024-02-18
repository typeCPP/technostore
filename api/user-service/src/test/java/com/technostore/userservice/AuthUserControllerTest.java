package com.technostore.userservice;

import com.technostore.userservice.controller.UserController;
import com.technostore.userservice.model.User;
import com.technostore.userservice.repository.UserRepository;
import com.technostore.userservice.repository.UserTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static com.technostore.userservice.TestUtils.getFileContent;
import static com.technostore.userservice.UserTestFactory.buildUser;
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
}
