package com.project.commercebank2024.controller;

import com.project.commercebank2024.domain.AppInfo;
import com.project.commercebank2024.domain.UserInfo;
import com.project.commercebank2024.service.AppInfoService;
import com.project.commercebank2024.service.ServerInfoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/appInfo")
@CrossOrigin
public class AppInfoController {
    @Autowired
    private final AppInfoService appInfoService;
    @Autowired
    private final ServerInfoService serverInfoService;

    /*@CrossOrigin
    @PostMapping("/appInfo")
    public ResponseEntity<?> save(@RequestBody AppInfo appInfo){
        //Assume that admin user is already logged in to the system
        Long userId = 1L;
        return new ResponseEntity<>(appInfoService.create(appInfo, userId), HttpStatus.CREATED);
    }*/

    @GetMapping
    public ResponseEntity<List<AppInfo>> getAllApps(){return new ResponseEntity<List<AppInfo>>(appInfoService.allApps(), HttpStatus.OK);}
}
