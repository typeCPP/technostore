package com.technostore.userservice;

import com.technostore.userservice.model.User;
import com.technostore.userservice.repository.UserRepository;
import com.technostore.userservice.repository.VerifyCodeRepository;
import com.technostore.userservice.service.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static com.technostore.userservice.UserTestFactory.buildUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MailControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VerifyCodeRepository verifyCodeRepository;
    @MockBean
    JavaMailSender javaMailSender;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @SpyBean
    MailService mailService;

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

        mockMvc.perform(get("/send-code-for-confirmation-account?id=" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        assertThat(verifyCodeRepository.findVerifyCodeByUser(user.getId())).isNotNull();
    }

    @Test
    void sendConfirmationAccountCodeWhenUserNotExistsTest() throws Exception {
        mockMvc.perform(get("/send-code-for-confirmation-account?id=" + 10000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void sendConfirmationAccountCodeWhenExceptionBySendServiceTest() throws Exception {
        User user = userRepository.save(buildUser());
        doThrow(MessagingException.class).when(mailService).sendConfirmationCode(user.getId());

        mockMvc.perform(get("/send-code-for-confirmation-account?id=" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void sendPasswordRecoveryCodeTest() throws Exception {
        User user = userRepository.save(buildUser());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        mockMvc.perform(get("/send-code-for-recovery-password?email=" + user.getEmail())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        assertThat(verifyCodeRepository.findVerifyCodeByUser(user.getId())).isNotNull();
    }

    @Test
    void sendPasswordRecoveryCodeWhenUserNotExistsTest() throws Exception {
        mockMvc.perform(get("/send-code-for-recovery-password?email=" + "strangeEmail")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void sendPasswordRecoveryCodeWhenExceptionBySendServiceTest() throws Exception {
        User user = userRepository.save(buildUser());
        doThrow(MessagingException.class).when(mailService).sendPasswordRecoveryCode(user.getEmail());

        mockMvc.perform(get("/send-code-for-recovery-password?email=" + user.getEmail())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }
}
