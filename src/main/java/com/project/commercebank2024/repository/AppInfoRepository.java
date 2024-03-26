package com.project.commercebank2024.repository;

import com.project.commercebank2024.domain.AppInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AppInfoRepository extends JpaRepository<AppInfo, Long> {
    AppInfo findByappInfoId(Long appInfoId);
    //this helps to find user_apps that are associated with the uid
    List<AppInfo> findByUserApps_UserInfo_uId(Long uId);

}
