package com.technostore.userservice;

import com.technostore.userservice.model.User;
import com.technostore.userservice.repository.UserRepository;
import com.technostore.userservice.repository.VerifyCodeRepository;
import com.technostore.userservice.service.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static com.technostore.userservice.UserTestFactory.buildUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MailServiceTest {
    @Autowired
    MailService mailService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VerifyCodeRepository verifyCodeRepository;
    @MockBean
    JavaMailSender javaMailSender;
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
    void sendConfirmationAccountCodeTest() throws Exception {
        User user = userRepository.save(buildUser());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        mailService.sendConfirmationCode(user.getId());
        assertThat(verifyCodeRepository.findVerifyCodeByUser(user.getId())).isNotNull();
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendPasswordRecoveryCodeTest() throws Exception {
        User user = userRepository.save(buildUser());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        mailService.sendPasswordRecoveryCode(user.getEmail());
        assertThat(verifyCodeRepository.findVerifyCodeByUser(user.getId())).isNotNull();
        verify(javaMailSender).send(any(MimeMessage.class));
    }
}
