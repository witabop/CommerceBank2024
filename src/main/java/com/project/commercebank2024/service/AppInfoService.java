package com.project.commercebank2024.service;

import com.project.commercebank2024.domain.AppInfo;
import com.project.commercebank2024.domain.UserInfo;
import com.project.commercebank2024.repository.AppInfoRepository;
import com.project.commercebank2024.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppInfoService {
    @Autowired
    private final UserInfoRepository userInfoRepository;
    @Autowired
    private final AppInfoRepository appInfoRepository;

    public List<AppInfo> allApps(){return appInfoRepository.findAll();}
    public Optional<AppInfo> findById(Long id){return appInfoRepository.findById(id);}

    public AppInfo create(AppInfo appInfo, Long userId){
        UserInfo userInfo = userInfoRepository.findByuId(userId);
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setUId(userId);
            // Set other properties if needed
            userInfoRepository.save(userInfo);
        }

        return appInfoRepository.save(appInfo);
    }

}
