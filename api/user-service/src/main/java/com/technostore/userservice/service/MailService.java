package com.technostore.userservice.service;

import javax.mail.MessagingException;

public interface MailService {
    void sendConfirmationCode(Long id) throws MessagingException;
}