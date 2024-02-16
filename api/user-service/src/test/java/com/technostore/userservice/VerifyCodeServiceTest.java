package com.technostore.userservice;

import com.technostore.userservice.model.User;
import com.technostore.userservice.model.VerifyCode;
import com.technostore.userservice.repository.UserRepository;
import com.technostore.userservice.repository.VerifyCodeRepository;
import com.technostore.userservice.service.VerifyCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VerifyCodeServiceTest {
    @Autowired
    VerifyCodeService verifyCodeService;
    @Autowired
    VerifyCodeRepository verifyCodeRepository;
    @Autowired
    UserRepository userRepository;

    final static String TEST_VERIFY_CODE = "123456";
    @Test
    void findVerifyCodeByUserTest() {
        User user = saveTestUser();
        verifyCodeRepository.save(VerifyCode.builder()
                .user(user)
                .code(TEST_VERIFY_CODE)
                .build());

        String code = verifyCodeService.findVerifyCodeByUser(user);
        assertThat(code).isEqualTo(TEST_VERIFY_CODE);
    }

    @Test
    void findVerifyCodeByUserErrorTest() {
        User user = saveTestUser();
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> verifyCodeService.findVerifyCodeByUser(user));
    }

    @Test
    void deleteVerifyCodeByUserTest() {
        User user = saveTestUser();
        VerifyCode code = verifyCodeRepository.save(VerifyCode.builder()
                .user(user)
                .code(TEST_VERIFY_CODE)
                .build());

        assertThat(verifyCodeRepository.findById(code.getId()).isPresent()).isTrue();
        verifyCodeService.deleteVerifyCodeByUser(user);
        assertThat(verifyCodeRepository.findById(code.getId()).isPresent()).isFalse();
    }

    @Test
    void findVerifyCodeByUserAndCodeTest() {
        User user = saveTestUser();
        VerifyCode code = verifyCodeRepository.save(VerifyCode.builder()
                .user(user)
                .code(TEST_VERIFY_CODE)
                .build());

        verifyCodeService.findVerifyCodeByUserAndCode(user.getEmail(), code.getCode());
    }

    @Test
    void findVerifyCodeByUserAndCodeErrorTest() {
        User user = saveTestUser();
        VerifyCode code = verifyCodeRepository.save(VerifyCode.builder()
                .user(user)
                .code(TEST_VERIFY_CODE)
                .build());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> verifyCodeService
                        .findVerifyCodeByUserAndCode(user.getEmail(), code.getCode() + "wrong code"));
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
