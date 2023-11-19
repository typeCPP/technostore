package com.technostore.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.technostore.userservice.model.User;
import com.technostore.userservice.model.VerifyCode;
import com.technostore.userservice.repository.VerifyCodeRepository;

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Autowired
    VerifyCodeRepository verifyCodeRepository;

    @Override
    public void deleteVerifyCodeByUser(User user) {
        verifyCodeRepository.deleteVerifyCodeByUser(user.getId());
    }

    @Override
    public void saveCode(VerifyCode verifyCode) {
        verifyCodeRepository.save(verifyCode);
    }
}
