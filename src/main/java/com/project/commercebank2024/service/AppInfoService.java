package com.project.commercebank2024.service;

import com.project.commercebank2024.domain.AppInfo;
import com.project.commercebank2024.domain.UserInfo;
import com.project.commercebank2024.repository.AppInfoRepository;
import com.project.commercebank2024.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppInfoService {
    private static final Logger logger = LoggerFactory.getLogger(AppInfoService.class);
    private final UserInfoRepository userInfoRepository;
    private final AppInfoRepository appInfoRepository;
    public AppInfo create(AppInfo appInfo, String userId){
        UserInfo userInfo = userInfoRepository.findByUserId(userId);
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setUserId(userId);
            // Set other properties if needed
            userInfoRepository.save(userInfo);
        }
        appInfo.setUserInfo(userInfo);

        logger.info("User Information:");
        logger.info("ID: {}", userInfo.getUserId());
        logger.info("Password: {}", userInfo.getPassword());
        logger.info("Role: {}", userInfo.getRole());
        /*System.out.println("  --- user ---  ");
        System.out.println("ID " + userInfo.getUserId());
        System.out.println("Password " + userInfo.getPassword());
        System.out.println("Role " + userInfo.getRole());*/
        return appInfoRepository.save(appInfo);
    }
}
