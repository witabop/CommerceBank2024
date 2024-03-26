package com.project.commercebank2024.service;

import com.project.commercebank2024.domain.AppInfo;
import com.project.commercebank2024.domain.UserInfo;
import com.project.commercebank2024.repository.AppInfoRepository;
import com.project.commercebank2024.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AppInfoService {
    //hibernate:
    //ddl-auto: create
    private final UserInfoRepository userInfoRepository;
    private final AppInfoRepository appInfoRepository;

    public AppInfo create(AppInfo appInfo, Long userId){
        UserInfo userInfo = userInfoRepository.findByuId(userId);
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setUId(userId);
            // Set other properties if needed
            userInfoRepository.save(userInfo);
        }

        System.out.println("User Information:");
        System.out.println("  --- user ---  ");
        System.out.println("ID " + userInfo.getUserName());
        System.out.println("Password " + userInfo.getPassword());
        System.out.println("Role " + userInfo.getRole());
        return appInfoRepository.save(appInfo);
    }

    public List<AppInfo> getUserApps(Long userId){
        return appInfoRepository.findByUserApps_UserInfo_uId(userId);
    }
}
