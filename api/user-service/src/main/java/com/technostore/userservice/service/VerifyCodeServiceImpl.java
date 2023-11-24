package com.technostore.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.technostore.userservice.model.User;
import com.technostore.userservice.model.VerifyCode;
import com.technostore.userservice.repository.VerifyCodeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Autowired
    VerifyCodeRepository verifyCodeRepository;

    @Override
    public VerifyCode findVerifyCodeByUser(User user) {
        Optional<VerifyCode> passwordRecoveryCodeOptional =
                verifyCodeRepository.findVerifyCodeByUser(user);
        if (passwordRecoveryCodeOptional.isPresent()) {
            return passwordRecoveryCodeOptional.get();
        }
        throw new EntityNotFoundException("Code for this user does not exist.");
    }

    @Override
    public void deleteVerifyCodeByUser(User user) {
        verifyCodeRepository.deleteVerifyCodeByUser(user.getId());
    }

    @Override
    public void saveCode(VerifyCode verifyCode) {
        verifyCodeRepository.save(verifyCode);
    }

    @Override
    public VerifyCode findVerifyCodeByUserAndCode(String email, String code) {
        List<VerifyCode> list = verifyCodeRepository.findVerifyCodesByCode(code);
        for (VerifyCode verifyCode : list) {
            if (verifyCode.getUser().getEmail().equals(email)) {
                return verifyCode;
            }
        }
        throw new EntityNotFoundException("Such code does not exist.");
    }
}
