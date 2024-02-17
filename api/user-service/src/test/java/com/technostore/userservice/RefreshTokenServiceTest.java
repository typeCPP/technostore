package com.technostore.userservice;

import com.technostore.userservice.model.RefreshToken;
import com.technostore.userservice.model.User;
import com.technostore.userservice.repository.RefreshTokenRepository;
import com.technostore.userservice.repository.UserRepository;
import com.technostore.userservice.service.RefreshTokenService;
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
public class RefreshTokenServiceTest {
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserRepository userRepository;

    final static String TEST_REFRESH_TOKEN = "test refresh token";

    @Test
    void addRefreshTokenTest() {
        User user = saveTestUser();

        refreshTokenService.addRefreshToken(user, TEST_REFRESH_TOKEN);

        List<RefreshToken> list = refreshTokenRepository.findAllByUser(user);

        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getToken()).isEqualTo(TEST_REFRESH_TOKEN);
        assertThat(list.get(0).getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void isCorrectRefreshTokenTest() {
        User user = saveTestUser();
        refreshTokenRepository.save(RefreshToken.builder()
                .token(TEST_REFRESH_TOKEN)
                .user(user)
                .build());

        assertThat(refreshTokenService.isCorrectRefreshToken(user, TEST_REFRESH_TOKEN)).isTrue();
        assertThat(refreshTokenService.isCorrectRefreshToken(user, "wrong token")).isFalse();
    }

    @Test
    void updateRefreshTokenTest() {
        User user = saveTestUser();
        refreshTokenRepository.save(RefreshToken.builder()
                .token(TEST_REFRESH_TOKEN)
                .user(user)
                .build());
        Set<RefreshToken> tokens = new HashSet<RefreshToken>(refreshTokenRepository.findAllByUser(user));
        user.setRefreshTokens(tokens);

        assertThat(refreshTokenService.updateRefreshToken(
                user,
                TEST_REFRESH_TOKEN + "2",
                "wrong old token"))
                .isFalse();
        assertThat(refreshTokenService.updateRefreshToken(
                user,
                TEST_REFRESH_TOKEN + "2",
                TEST_REFRESH_TOKEN))
                .isTrue();

        List<RefreshToken> list = refreshTokenRepository.findAllByUser(user);
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getToken()).isEqualTo(TEST_REFRESH_TOKEN + "2");
        assertThat(list.get(0).getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void deleteRefreshTokenTest() {
        User user = saveTestUser();
        refreshTokenRepository.save(RefreshToken.builder()
                .token(TEST_REFRESH_TOKEN)
                .user(user)
                .build());
        refreshTokenService.deleteRefreshToken(user, TEST_REFRESH_TOKEN);
        List<RefreshToken> list = refreshTokenRepository.findAllByUser(user);
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    void deleteAllRefreshTokensByUser() {
        User user = saveTestUser();
        refreshTokenRepository.save(RefreshToken.builder()
                .token(TEST_REFRESH_TOKEN)
                .user(user)
                .build());
        refreshTokenRepository.save(RefreshToken.builder()
                .token(TEST_REFRESH_TOKEN + "2")
                .user(user)
                .build());

        refreshTokenService.deleteAllRefreshTokensByUser(user);
        List<RefreshToken> list = refreshTokenRepository.findAllByUser(user);
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
