//a lot of this controller code is just complete and utter shit i copied based on what we did in class for UserInfo and AppInfo
//i have no fucking idea if were ever gonna need them, but theyre here
//i could prolly make our lives easier in regards to database fetching
//by just adding in some functions into all of these controllers/services, but it works for now so fuck it
//if anyone has suggestions/ideas please say something, cause some of this is so inefficient
package com.project.commercebank2024.controller;

import com.project.commercebank2024.domain.AppInfo;
import com.project.commercebank2024.domain.UserApps;
import com.project.commercebank2024.domain.UserInfo;
import com.project.commercebank2024.repository.UserAppsRepository;
import com.project.commercebank2024.service.AppInfoService;
import com.project.commercebank2024.service.UserAppsService;
import com.project.commercebank2024.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/userapps")
@CrossOrigin
public class UserAppsController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserAppsService userAppsService;
    @Autowired
    private final AppInfoService appInfoService;
    @Autowired
    private final UserAppsRepository userAppsRepository;

    @GetMapping
    public ResponseEntity<List<UserApps>> getAllUserApps(){return new ResponseEntity<List<UserApps>>(userAppsService.allUserApps(), HttpStatus.OK);}

    public class UsersAppsResponse{
        @Getter
        @Setter
        private Long appId;
        @Getter
        @Setter
        private String application;

        public UsersAppsResponse(){}
        public UsersAppsResponse(Long appId, String application){
            this.appId = appId;
            this.application = application;
        }

    }
    @GetMapping("/{uId}")
    public ResponseEntity<?> getUserApp(@PathVariable Long uId){
        Optional<UserInfo> user = userService.singleUser(uId);
        List<?> apps = user.get().getUserApps().parallelStream().map(userApps -> userApps.getAppInfo().getApp_desc()).collect(Collectors.toList());
        if(user.isPresent()){
            /*List<UsersAppsResponse> userAppDTOs = user.get().getUserApps().stream()
                    .map(userApps -> {
                        UsersAppsResponse dto = new UsersAppsResponse();
                        dto.setId(userApps.getAppInfo().getAppInfoId());
                        dto.setName(userApps.getAppInfo().getAppDesc());
                        return dto;
                    })
                    .collect(Collectors.toList());*/
            return new ResponseEntity<>(user.get().getUserApps(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("user not found", HttpStatus.OK);
        }
    }

    @PostMapping("/modifyusers")
    public ResponseEntity<?> modifyUsers(@RequestBody Map<String, ?> incomingModification){
        Long userId = Long.valueOf(incomingModification.get("uid").toString());
        Optional<UserInfo> optionalUserInfo = userService.singleUser(userId);
        if(!optionalUserInfo.isPresent()){
            return new ResponseEntity<>("No user found", HttpStatus.OK);
        }
        UserInfo userInfo = optionalUserInfo.get();
        List<Map<String, ?>> applications = (List<Map<String,?>>) incomingModification.get("application");

        try {
            for (Map<String, ?> app : applications) {
                Long appInfoId = Long.valueOf(app.get("appInfoId").toString());
                Optional<AppInfo> optionalAppInfo = appInfoService.findById(appInfoId);
                if (!optionalAppInfo.isPresent()) {
                    return new ResponseEntity<>("No appId found by that id", HttpStatus.OK);
                }
                AppInfo appInfo = optionalAppInfo.get();
                UserApps userApps = new UserApps();
                userApps.setUserInfo(userInfo);
                userApps.setAppInfo(appInfo);
                userApps.setAppInfo(appInfo);
                //Timestamp.valueOf(LocalDateTime.parse(createdAtStr, DateTimeFormatter.ISO_DATE_TIME));
                OffsetDateTime currTime = OffsetDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

                userApps.setCreatedAt(Timestamp.valueOf(currTime.format(formatter)));
                userApps.setModifiedAt((Timestamp.valueOf(currTime.format(formatter))));
                userApps.setUserAppsUid(Long.valueOf(app.get("user_apps_uid").toString()));
                userApps.setCreatedBy(app.get("createdBy").toString());
                userApps.setModifiedBy(app.get("modifiedBy").toString());
                userAppsRepository.save(userApps);
                userInfo.addUserApps(userApps);
            }
            return new ResponseEntity<>("Userapps done successfully", HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>("Error \n" + e, HttpStatus.OK);
        }

    }
}
