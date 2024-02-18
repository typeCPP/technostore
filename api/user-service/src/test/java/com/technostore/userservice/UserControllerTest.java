package com.technostore.userservice;

import com.technostore.userservice.model.User;
import com.technostore.userservice.model.VerifyCode;
import com.technostore.userservice.repository.UserRepository;
import com.technostore.userservice.service.UserService;
import com.technostore.userservice.service.VerifyCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static com.technostore.userservice.TestUtils.getFileContent;
import static com.technostore.userservice.UserTestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    VerifyCodeService verifyCodeService;
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
    void getUserByIdTest() throws Exception {
        User user = userRepository.save(buildUser());

        mockMvc.perform(get("/user/id/" + user.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(String.format(getFileContent("controller/user/user.json"),
                        user.getId()), true));
    }

    @Test
    void getUserByIdWhenUserNotExistsTest() throws Exception {
        mockMvc.perform(get("/user/id/" + 10000))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("User with id 10000 does not exist.")));
    }

    @Test
    void checkEmailExistsTest() throws Exception {
        mockMvc.perform(get("/user/check-email-exists?email=emailemailemail"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json("{\"exists\":false}", true));
    }

    @Test
    void tryPasswordRecoveryWhenUserWithThisEmailNotExistsTest() throws Exception {
        mockMvc.perform(get("/user/password-recovery?email=inastianice&code=567765"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("User with email inastianice does not exist.")));
    }

    @Test
    void tryPasswordRecoveryWhenCodeNotExistsTest() throws Exception {
        User user = userRepository.save(buildUser());
        mockMvc.perform(get("/user/password-recovery?email=" + user.getEmail() + "&code=567765"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Code for user with email does not exist.")));
    }

    @Test
    void tryPasswordRecoveryWhenIncorrectCodeTest() throws Exception {
        User user = userRepository.save(buildUser());
        verifyCodeService.saveCode(VerifyCode.builder().user(user).code("6543765").build());

        mockMvc.perform(get("/user/password-recovery?email=" + user.getEmail() + "&code=111111"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Invalid code for user with email email")));
    }

    @Test
    void passwordRecoveryTest() throws Exception {
        User user = userRepository.save(buildUser());
        verifyCodeService.saveCode(VerifyCode.builder().user(user).code("6543765").build());

        mockMvc.perform(get("/user/password-recovery?email=" + user.getEmail() + "&code=6543765"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(String.valueOf(user.getId()))))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void confirmUserAccountWhenAccountWithThisEmailAlreadyExistsTest() throws Exception {
        User user = userRepository.save(buildUser());

        mockMvc.perform(get("/user/confirm-account?email=" + user.getEmail() + "&confirmationCode=6543765"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("This email is already in use")));
    }

    @Test
    void confirmUserAccountWhenCodeNotExistsTest() throws Exception {
        User user = userRepository.save(buildUnverifiedUser("test-email"));

        mockMvc.perform(get("/user/confirm-account?email=" + user.getEmail() + "&confirmationCode=6543765"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Code 6543765 for user with test-email does not exist.")));
    }

    @Test
    void confirmUserAccountTest() throws Exception {
        User user = userRepository.save(buildUnverifiedUser("test-email"));
        verifyCodeService.saveCode(VerifyCode.builder().user(user).code("6543765").build());

        mockMvc.perform(get("/user/confirm-account?email=" + user.getEmail() + "&confirmationCode=6543765"))
                .andExpect(status().is2xxSuccessful());
        assertThat(userRepository.findById(user.getId()).get().isEnabled()).isTrue();
    }

    @Test
    void loginTest() throws Exception {
        User user = userService.registerUser(buildRegisterBean());
        user.setEnabled(true);
        userRepository.save(user);

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getFileContent("controller/user/login-request.json")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(String.valueOf(user.getId()))))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void loginWhenUserWithThisEmailNotExistsTest() throws Exception {
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getFileContent("controller/user/login-request.json")))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("The user with such email does not exist.")));
    }

    @Test
    void loginWithWrongPasswordTest() throws Exception {
        User user = userService.registerUser(buildRegisterBean());
        user.setEnabled(true);
        userRepository.save(user);

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getFileContent("controller/user/login-request-with-wrong-password.json")))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Wrong password!")));
    }
}
