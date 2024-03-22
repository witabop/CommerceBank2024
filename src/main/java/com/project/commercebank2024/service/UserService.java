package com.project.commercebank2024.service;

import com.project.commercebank2024.domain.UserInfo;
import com.project.commercebank2024.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserService {

    private final UserInfoRepository userInfoRepository;
    public UserInfo create(UserInfo user){
        return userInfoRepository.save(user);
    }

    // public UserInfo auth(String username, String password){
    //     return userInfoRepository.check(user);
    // }
}
