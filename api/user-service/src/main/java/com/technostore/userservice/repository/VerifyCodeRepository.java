package com.technostore.userservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.technostore.userservice.model.User;
import com.technostore.userservice.model.VerifyCode;

@Repository
public interface VerifyCodeRepository extends JpaRepository<VerifyCode, Long> {
    @Query(value = "DELETE FROM VerifyCode v WHERE v.user.id=:userId")
    @Modifying
    @Transactional
    void deleteVerifyCodeByUser(Long userId);
}
