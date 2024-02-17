package com.technostore.userservice;

import com.technostore.userservice.model.UserToken;
import com.technostore.userservice.model.User;
import com.technostore.userservice.repository.UserRepository;
import com.technostore.userservice.repository.UserTokenRepository;
import com.technostore.userservice.service.UserTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTokenServiceTest {
    @Autowired
    UserTokenService userTokenService;
    @Autowired
    UserTokenRepository userTokenRepository;
    @Autowired
    UserRepository userRepository;

    final static String TEST_ACCESS_TOKEN = "test access token";

    @Test
    void addAccessTokenTest() {
        User user = saveTestUser();
        userTokenService.addAccessToken(user, TEST_ACCESS_TOKEN);
        List<UserToken> userTokenList = userTokenRepository.findAllByUser(user);

        assertThat(userTokenList.size()).isEqualTo(1);
        assertThat(userTokenList.get(0).getToken()).isEqualTo(TEST_ACCESS_TOKEN);
        assertThat(userTokenList.get(0).getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void isCorrectAccessTokenTest() {
        User user = saveTestUser();

        userTokenRepository.save(UserToken.builder()
                .token(TEST_ACCESS_TOKEN)
                .user(user)
                .build());

        assertThat(userTokenService.isCorrectAccessToken(user, TEST_ACCESS_TOKEN)).isTrue();
        assertThat(userTokenService.isCorrectAccessToken(user, "wrong token")).isFalse();
    }

    @Test
    void updateUserTokenTest() {
        User user = saveTestUser();
        userTokenRepository.save(UserToken.builder()
                .token(TEST_ACCESS_TOKEN)
                .user(user)
                .build());
        Set<UserToken> tokens = new HashSet<UserToken>(userTokenRepository.findAllByUser(user));
        user.setUserTokens(tokens);

        assertThat(userTokenService.updateAccessToken(
                user,
                TEST_ACCESS_TOKEN + "2",
                "wrong old token"))
                .isFalse();
        assertThat(userTokenService.updateAccessToken(
                user,
                TEST_ACCESS_TOKEN + "2",
                TEST_ACCESS_TOKEN))
                .isTrue();

        List<UserToken> list = userTokenRepository.findAllByUser(user);
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getToken()).isEqualTo(TEST_ACCESS_TOKEN + "2");
        assertThat(list.get(0).getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void deleteUserTokenTest() {
        User user = saveTestUser();
        userTokenRepository.save(UserToken.builder()
                .token(TEST_ACCESS_TOKEN)
                .user(user)
                .build());
        userTokenService.deleteAccessToken(user, TEST_ACCESS_TOKEN);
        List<UserToken> list = userTokenRepository.findAllByUser(user);
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    void deleteAllUserTokensByUser() {
        User user = saveTestUser();
        userTokenRepository.save(UserToken.builder()
                .token(TEST_ACCESS_TOKEN)
                .user(user)
                .build());
        userTokenRepository.save(UserToken.builder()
                .token(TEST_ACCESS_TOKEN + "2")
                .user(user)
                .build());

        userTokenService.deleteAllAccessTokensByUser(user);
        List<UserToken> list = userTokenRepository.findAllByUser(user);
        assertThat(list.size()).isEqualTo(0);
    }

    User saveTestUser() {
        return userRepository.save(User.builder()
                .email("email")
                .password("pass")
                .name("some name")
                .lastName("last name")
                .linkPhoto("link.com")
                .isEnabled(true)
                .build());
    }
}
