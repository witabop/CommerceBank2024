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
import java.security.Key;
import java.sql.SQLOutput;
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

    @PostMapping("/add")
    public ResponseEntity<?> addUserApps(@RequestBody Map<String, ?> incomingAdditions){
        try {
            OffsetDateTime currTime = OffsetDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

            Long userId = Long.valueOf(incomingAdditions.get("uid").toString());
            Optional<UserInfo> optionalUserInfo = userService.singleUser(userId);
            UserInfo userInfo = optionalUserInfo.get();
            UserApps userApps = new UserApps();
            List<Map<String, ?>> newApp = (List<Map<String, ?>>) incomingAdditions.get("application");
            System.out.println(newApp.toString());
            for(Map<String, ?> info: newApp) {
                Long appInfoId = Long.valueOf(info.get("appInfoId").toString());
                Optional<AppInfo> optionalAppInfo = appInfoService.findById(appInfoId);
                AppInfo appInfo = optionalAppInfo.get();
                boolean alreadyExists = userInfo.getUserApps().stream()
                        .anyMatch(userApp -> userApp.getAppInfo().getAppInfoId().equals(appInfoId));

                if (alreadyExists) {
                    return new ResponseEntity<>("User already has this app", HttpStatus.OK);
                }
                Long userAppsUid = Long.valueOf(info.get("user_apps_uid").toString());
                String modifiedBy = info.get("modifiedBy").toString();
                String createdBy = info.get("createdBy").toString();
                Timestamp createdAt = Timestamp.valueOf(currTime.format(formatter));
                Timestamp modifiedAt = Timestamp.valueOf(currTime.format(formatter));
                userApps = new UserApps(userAppsUid, userInfo, appInfo, createdAt, "admin1", modifiedAt, "admin1");
            }
            userAppsRepository.save(userApps);
            //(Timestamp.valueOf(currTime.format(formatter)));
            //userApps.setModifiedAt((Timestamp.valueOf(currTime.format(formatter))));
            return new ResponseEntity<>("Test", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.toString(), HttpStatus.OK);
        }
    }


    @DeleteMapping("/delete/{uid}/{appId}")
    public ResponseEntity<?> deleteUserApp(@PathVariable Long uid, @PathVariable Long appId){
        try{
            Optional<UserInfo> optionalUserInfo = userService.singleUser(uid);
            Optional<AppInfo> optionalAppInfo = appInfoService.findById(appId);
            UserInfo userInfo= optionalUserInfo.get();
            AppInfo appInfo = optionalAppInfo.get();

            Optional<UserApps> userAppsOptional = userInfo.getUserApps().stream()
                    .filter(userApps -> userApps.getAppInfo().getAppInfoId().equals(appId))
                    .findFirst();
            UserApps userApps = userAppsOptional.get();
            userAppsRepository.delete(userApps);
            userInfo.getUserApps().remove(userApps);
            return new ResponseEntity<>("App removed from access", HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(e.toString(), HttpStatus.OK);
        }
    }
    /*@PostMapping("/modifyusers")
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

                OffsetDateTime currTime = OffsetDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

                userApps.setCreatedAt(Timestamp.valueOf(currTime.format(formatter)));
                userApps.setModifiedAt((Timestamp.valueOf(currTime.format(formatter))));
                userApps.setUserAppsUid(Long.valueOf(app.get("user_apps_uid").toString()));
                userApps.setCreatedBy(app.get("createdBy").toString());
                userApps.setModifiedBy(app.get("modifiedBy").toString());
                userAppsRepository.save(userApps);
                //userInfo.addUserApps(userApps);
            }
            return new ResponseEntity<>("Userapps done successfully", HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>("Error \n" + e, HttpStatus.OK);
        }

    }*/
}
