package com.technostore.userservice;

import com.technostore.userservice.model.User;
import com.technostore.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static com.technostore.userservice.TestUtils.getFileContent;
import static com.technostore.userservice.UserTestFactory.buildUser;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("DELETE FROM user.user_jwt;");
        jdbcTemplate.execute("DELETE FROM user.user_refresh_token;");
        jdbcTemplate.execute("DELETE FROM user.verify_code;");
        jdbcTemplate.execute("DELETE FROM user.users;");
    }

    @Test
    void getUserByIdTest() throws Exception{
        User user = userRepository.save(buildUser());

        mockMvc.perform(get("/user/id/" + user.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(String.format(getFileContent("controller/user/user.json"),
                        user.getId()), true));
    }

    @Test
    void getUserByIdWhenUserNotExistsTest() throws Exception{
        mockMvc.perform(get("/user/id/" + 10000))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("User with id 10000 does not exist.")));
    }
}
