package com.project.commercebank2024.repository;

import com.project.commercebank2024.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    // Needs a check function to validate a user by searching with username and password.
    UserInfo findByuId(Long userId);
    Optional<UserInfo> findByUserNameAndPassword(String username, String password);
}
