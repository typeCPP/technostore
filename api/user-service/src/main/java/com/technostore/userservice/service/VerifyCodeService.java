package com.technostore.userservice.service;

import com.technostore.userservice.model.User;
import com.technostore.userservice.model.VerifyCode;

public interface VerifyCodeService {
    String findVerifyCodeByUser(User user);

    void deleteVerifyCodeByUser(User user);

    void saveCode(VerifyCode verifyCode);

    VerifyCode findVerifyCodeByUserAndCode(String email, String code);
}
