package com.project.commercebank2024.service;

import com.project.commercebank2024.domain.AppInfo;
import com.project.commercebank2024.domain.UserInfo;
import com.project.commercebank2024.repository.AppInfoRepository;
import com.project.commercebank2024.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppInfoService {
    private final UserInfoRepository userInfoRepository;
    private final AppInfoRepository appInfoRepository;

    public AppInfo create(AppInfo appInfo, Long userId){
        UserInfo userInfo = userInfoRepository.findByUid(userId);
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setUid(userId);
            // Set other properties if needed
            userInfoRepository.save(userInfo);
        }
        appInfo.setUserInfo(userInfo);

        System.out.println("User Information:");
        //S ystem.out.println("ID: {}", userInfo.getUid());
        //System.out.println("Password: {}", userInfo.getPassword());
        //System.out.println("Role: {}", userInfo.getRole());
        System.out.println("  --- user ---  ");
        System.out.println("ID " + userInfo.getUserName());
        System.out.println("Password " + userInfo.getPassword());
        System.out.println("Role " + userInfo.getRole());
        return appInfoRepository.save(appInfo);
    }
}
