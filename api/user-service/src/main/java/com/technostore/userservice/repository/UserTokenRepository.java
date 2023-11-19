package com.technostore.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.technostore.userservice.model.UserToken;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
}
