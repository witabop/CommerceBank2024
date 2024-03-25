package com.project.commercebank2024.controller;

import com.project.commercebank2024.domain.AppInfo;
import com.project.commercebank2024.service.AppInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AppInfoController {
    private final AppInfoService appInfoService;

    @CrossOrigin
    @PostMapping("/appInfo")
    public ResponseEntity<?> save(@RequestBody AppInfo appInfo){
        //Assume that admin user is already logged in to the system
        Long userId = 1L;
        return new ResponseEntity<>(appInfoService.create(appInfo, userId), HttpStatus.CREATED);
    }
}