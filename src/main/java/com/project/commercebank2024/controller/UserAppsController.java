package com.project.commercebank2024.controller;

import com.project.commercebank2024.domain.UserApps;
import com.project.commercebank2024.service.UserAppsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/userapps")
public class UserAppsController {
    @Autowired
    private final UserAppsService userAppsService;

    public ResponseEntity<Optional<UserApps>> getUserApp(@PathVariable Long id){
        Optional<UserApps> userApps = userAppsService.singleUserApps(id);
        if(userApps.isPresent()){
            return ResponseEntity.ok(userApps);
        }else{
            return ResponseEntity.notFound().build();
        }

    }
}
