package com.technostore.userservice.service;

import com.technostore.userservice.model.User;
import com.technostore.userservice.model.VerifyCode;

public interface VerifyCodeService {
    void deleteVerifyCodeByUser(User user);

    void saveCode(VerifyCode verifyCode);
}
