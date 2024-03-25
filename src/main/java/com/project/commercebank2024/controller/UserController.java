package com.project.commercebank2024.controller;

import com.project.commercebank2024.domain.UserInfo;
import com.project.commercebank2024.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserInfo>> getAllUser(){
        return new ResponseEntity<List<UserInfo>>(userService.allUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserInfo>> getSingleUser(@PathVariable Long id){
        return new ResponseEntity<Optional<UserInfo>>(userService.singleUser(id), HttpStatus.OK);
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> credentials){
        String username = credentials.get("username");
        String password = credentials.get("password");
        Optional<UserInfo> authenticatedUser = userService.auth(username, password);
        if(authenticatedUser.isPresent()){
            return new ResponseEntity<>(authenticatedUser.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

}
