package com.project.commercebank2024.controller;

import com.project.commercebank2024.domain.UserApps;
import com.project.commercebank2024.service.UserAppsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/userapps")
@CrossOrigin
public class UserAppsController {
    @Autowired
    private final UserAppsService userAppsService;

    //this is a useless function for right now
    //i dont think well ever need to getAllUserApps available
    //just in case we do ill leave this here and modify it in the future
    @GetMapping
    public ResponseEntity<Optional<UserApps>> getUserApp(@PathVariable Long userAppsUid){
        Optional<UserApps> userApps = userAppsService.singleUserApps(userAppsUid);
        if(userApps.isPresent()){
            return ResponseEntity.ok(userApps);
        }else{
            return ResponseEntity.notFound().build();
        }

    }

    //this is just some boiler plate stuff for now, this is where im wanting to implement the remove users app functionality
    /*public ResponseEntity<Optional<UserApps>> removeUserApp(@PathVariable Long id){
        return null;
    }*/
}
