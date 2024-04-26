package com.project.commercebank2024.controller;

import com.project.commercebank2024.domain.AppInfo;
import com.project.commercebank2024.domain.ServerInfo;
import com.project.commercebank2024.domain.UserApps;
import com.project.commercebank2024.domain.UserInfo;
import com.project.commercebank2024.repository.AppInfoRepository;
import com.project.commercebank2024.repository.UserAppsRepository;
import com.project.commercebank2024.repository.UserInfoRepository;
import com.project.commercebank2024.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
@CrossOrigin
public class UserController {

        @Autowired
        private UserService userService;
        @Autowired
        private UserInfoRepository userInfoRepository;
        @Autowired
        private AppInfoRepository appInfoRepository;
        @Autowired
        private UserAppsRepository userAppsRepository;

        @GetMapping
        //this returns all users and the applications they have access to
        public ResponseEntity<List<UserResponse>> getAllUser() {
            List<UserInfo> users = userService.allUsers();
            List<UserResponse> userResponses = new ArrayList<>();
            for(UserInfo u : users){
                List<String> applications = u.getUserApps().parallelStream().map(userApps -> userApps.getAppInfo().getApp_desc()).collect(Collectors.toList());
                userResponses.add(new UserResponse(u.getUId(), applications));
            }
            return new ResponseEntity<>(userResponses, HttpStatus.OK);
        }

        //this is just another custom response class i created to make the responses easier to read and understand
        //just have the user id and a list of applications that they have access to in the form of string
        private static class UserResponse{
            @Getter
            private Long UId;
            @Getter
            private List<String> applications;

            public UserResponse(Long UId, List<String> applications){
                this.UId = UId;
                this.applications = applications;
            }
        }

        @GetMapping("/{id}")
        //this returns only one user id and the applications they have access to
        public ResponseEntity<?> getSingleUser(@PathVariable Long id) {
            Optional<UserInfo> user = userService.singleUser(id);
            if(!user.isPresent()){
                return new ResponseEntity<>("User not found", HttpStatus.OK);
            }
            //more java bullshit voodoo, this is terribly inefficient. it works for now though
            List<String> applications = user.get().getUserApps().parallelStream().map(userApps -> userApps.getAppInfo().getApp_desc()).collect(Collectors.toList());
            UserResponse userResponse = new UserResponse(user.get().getUId(), applications);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }

        //this is a simple data transfer object that represents the response format for the authentication requests
        // basically this is going to be formatted to take in the json data that the http request is going to send us back
        static class AuthenticationResponse {
            @Getter
            private Long UID;
            @Getter
            private boolean authenticated;
            private boolean isAdmin;
            @Getter
            private Map<Long, String> applications;
            public boolean isAdmin() {
                return isAdmin;
            }

            public AuthenticationResponse(Long UID, boolean authenticated, boolean isAdmin, Map<Long, String> applications) {
                this.UID = UID;
                this.authenticated = authenticated;
                this.isAdmin = isAdmin;
                this.applications = applications;
            }
        }

        //this is the info needed to be returned from this
        //{ UID: 2, authenticated: true, isAdmin: false,
        //applications: ['API', 'PUP', 'RFS', 'TBD', 'INF', 'MQS'] }
        @PostMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> authenticate(@RequestBody Map<String, String> credentials) {
            Map<Long, String> applications = new HashMap<>();
            AuthenticationResponse response;
            OffsetDateTime currTime = OffsetDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            //get the username and password from the payload that is incoming, meaning the JSON format data
            String username = credentials.get("username");
            String password = credentials.get("password");
            //check if the user is authenticated and a valid user in the database
            Optional<UserInfo> authenticatedUser = userService.auth(username, password);
            if (authenticatedUser.isPresent()) {
                //simply just if the user is authenticated, which if they're able to log in obviously they are
                //then see if they are admin by using string equals method to compare admin string to the role string of the user
                UserInfo user = authenticatedUser.get();
                boolean isAdmin = "admin".equals(user.getRole());

                //this block is to check if the person calling this function is an admin or not
                //if they are an admin, we simply have applications return all apps possibly available as an admin would have access to everything
                if(isAdmin){
                    List<AppInfo> adminApps = appInfoRepository.findAll();
                    List<UserApps> adminsUserApps = new ArrayList<>();
                    for(AppInfo app : adminApps){
                        boolean hasAccess = user.getUserApps().stream()
                                .anyMatch(userApp -> userApp.getAppInfo().getAppInfoId().equals(app.getAppInfoId()));
                        if(!hasAccess) {
                            UserApps userApp = new UserApps(
                                    new Random().nextLong(),
                                    user,
                                    app,
                                    Timestamp.valueOf(currTime.format(formatter)),
                                    "admin",
                                    Timestamp.valueOf(currTime.format(formatter)),
                                    "admin"
                            );

                            userAppsRepository.save(userApp);
                            //user.addUserApps(userApp);
                        }
                        applications.put(app.getAppInfoId(), app.getApp_desc());
                    }
                    user.setUserApps(adminsUserApps);
                    response = new AuthenticationResponse(user.getUId(), true, isAdmin, applications);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }else {
                    //this is some voodoo java magic shit
                    //basically, we get a UserApps that are associated with the user, then we use .stream() to convert the list into a stream
                    //as this gives better way of processing collections in a 'functional style'
                    //then we use .map() to apply a transformation to each element of the stream, for every userapps object we get the corresponding appinfo object via
                    //getAppInf() and get that apps description from the found AppInfo object
                    //finally, collect the transformed elements of stream to a new list of strings via collectors.toList()
                List<UserApps> userAppsList = user.getUserApps();
                userAppsList.parallelStream()
                        .map(UserApps::getAppInfo)
                        .forEach(appInfo -> applications.put(appInfo.getAppInfoId(), appInfo.getApp_desc()));
                for(UserApps app: userAppsList){
                    applications.put(app.getAppInfo().getAppInfoId(), app.getAppInfo().getApp_desc());
                }
                    response = new AuthenticationResponse(user.getUId(), true, isAdmin, applications);
                }
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else { //if user isnt present then deny them 'entry'
                response = new AuthenticationResponse(null, false, false, new HashMap<>());
                return ResponseEntity.ok(response);
            }
        }
}
