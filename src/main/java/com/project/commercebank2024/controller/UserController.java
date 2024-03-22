package com.project.commercebank2024.controller;

import com.project.commercebank2024.domain.UserInfo;
import com.project.commercebank2024.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @CrossOrigin
    @PostMapping("/user")
    public ResponseEntity<?> save(@RequestBody UserInfo userInfo){
        return new ResponseEntity<>(userService.create(userInfo), HttpStatus.CREATED);
    }

}
