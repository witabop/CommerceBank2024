package com.project.commercebank2024.controller;

import com.project.commercebank2024.domain.ServerInfo;
import com.project.commercebank2024.domain.UserInfo;
import com.project.commercebank2024.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
@CrossOrigin
public class UserController {

        @Autowired
        private UserService userService;

        @GetMapping
        public ResponseEntity<List<UserInfo>> getAllUser() {return new ResponseEntity<List<UserInfo>>(userService.allUsers(), HttpStatus.OK);}

        @GetMapping("/{id}")
        public ResponseEntity<Optional<UserInfo>> getSingleUser(@PathVariable Long id) {return new ResponseEntity<Optional<UserInfo>>(userService.singleUser(id), HttpStatus.OK);}

        /*@GetMapping("/{id}/servers")
        public ResponseEntity<Optional<ServerInfo>> getServers(@PathVariable Long id){
            return new ResponseEntity<Optional<ServerInfo>>();
        } */


        //this is a simple data transfer object that represents the response format for the authentication requests
        // basically this is going to be formatted to take in the json data that the http request is going to send us back
        static class AuthenticationResponse {
            @Getter
            private Long UID;
            @Getter
            private boolean authenticated;
            private boolean isAdmin;
            @Getter
            private List<String> applications;
            public boolean isAdmin() {
                return isAdmin;
            }

            public AuthenticationResponse(Long UID, boolean authenticated, boolean isAdmin, List<String> applications) {
                this.UID = UID;
                this.authenticated = authenticated;
                this.isAdmin = isAdmin;
                this.applications = applications;
            }
        }

        //this is the info needed to be returned from this
        //{ UID: 2, authenticated: true, isAdmin: false,
        //  applications: ['API', 'PUP', 'RFS', 'TBD', 'INF', 'MQS'] }
        @PostMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> authenticate(@RequestBody Map<String, String> credentials) {
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
                //this is some voodoo java magic shit
                //basically, we get a UserApps that are associated with the user, then we use .stream() to convert the list into a stream
                //as this gives better way of processing collections in a 'functional style'
                //then we use .map() to apply a transformation to each element of the stream, for every userapps object we get the corresponding appinfo object via
                //getAppInf() and get that apps description from the found AppInfo object
                //finally, collect the transformed elements of stream to a new list of strings via collectors.toList()
                List<String> applications = user.getUserApps().stream().map(userApps -> userApps.getAppInfo().getApp_desc()).collect(Collectors.toList());
                AuthenticationResponse response = new AuthenticationResponse(user.getUId(), true, isAdmin, applications);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else { //if user isnt present then deny them 'entry'
                AuthenticationResponse response = new AuthenticationResponse(null, false, false, Collections.emptyList());
                return ResponseEntity.ok(response);
            }
        }
}
