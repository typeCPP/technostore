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
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static com.technostore.userservice.TestUtils.getFileContent;
import static com.technostore.userservice.UserTestFactory.buildRegisterBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TokenControllerTest {
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
    void refreshTokensTest() throws Exception {
        User user = userService.registerUser(buildRegisterBean());
        user.setEnabled(true);
        userRepository.save(user);
        Map<String, String> map = userController.generateMapWithInfoAboutTokens(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + map.get("access-token"));

        mockMvc.perform(post("/refresh-tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format(getFileContent("controller/token/refresh-token-request.json"),
                                map.get("access-token"), map.get("refresh-token")))
                        .headers(headers))
                .andExpect(status().is2xxSuccessful());
        assertThat(userTokenRepository.findAllByUser(user).get(0).getToken()).isNotEqualTo(map.get("access-token"));
    }

    @Test
    void refreshTokensWhenNotExistingRefreshTokenInRequestTest() throws Exception {
        User user = userService.registerUser(buildRegisterBean());
        user.setEnabled(true);
        userRepository.save(user);
        Map<String, String> map = userController.generateMapWithInfoAboutTokens(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + map.get("access-token"));

        mockMvc.perform(post("/refresh-tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format(getFileContent("controller/token/refresh-token-request.json"),
                                map.get("access-token"), map.get("access-token")))
                        .headers(headers))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("This refresh token does not exist.")));
    }

    @Test
    void refreshTokensWhenNotExistingAccessTokenInRequestTest() throws Exception {
        User user = userService.registerUser(buildRegisterBean());
        user.setEnabled(true);
        userRepository.save(user);
        Map<String, String> map = userController.generateMapWithInfoAboutTokens(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + map.get("access-token"));

        mockMvc.perform(post("/refresh-tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format(getFileContent("controller/token/refresh-token-request.json"),
                                map.get("refresh-token"), map.get("refresh-token")))
                        .headers(headers))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("This access token does not exist.")));
    }
}
