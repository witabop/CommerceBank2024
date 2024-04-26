package com.project.commercebank2024.service;

import com.project.commercebank2024.domain.UserInfo;
import com.project.commercebank2024.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private UserInfoRepository userInfoRepository;
    public UserInfo create(UserInfo user){
        return userInfoRepository.save(user);
    }

    public List<UserInfo> allUsers(){return userInfoRepository.findAll();}

    public Optional<UserInfo> singleUser(Long id){
        return userInfoRepository.findById(id);
    }

    public Optional<UserInfo> auth(String username, String password){return userInfoRepository.findByUserNameAndPassword(username, password);}
    public UserInfo save(UserInfo userInfo){return userInfoRepository.save(userInfo);}
}
