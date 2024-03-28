package com.project.commercebank2024.controller;

import com.project.commercebank2024.domain.AppInfo;
import com.project.commercebank2024.domain.UserApps;
import com.project.commercebank2024.domain.UserInfo;
import com.project.commercebank2024.service.UserAppsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/userapps")
@CrossOrigin
public class UserAppsController {
    @Autowired
    private final UserAppsService userAppsService;

    @GetMapping
    public ResponseEntity<List<UserApps>> getAllUserApps(){return new ResponseEntity<List<UserApps>>(userAppsService.allUserApps(), HttpStatus.OK);}

    @GetMapping("/{uId}")
    public ResponseEntity<Optional<UserApps>> getUserApp(@PathVariable Long uId){
        Optional<UserApps> userApps = userAppsService.singleUserApps(uId);
        if(userApps.isPresent()){
            return ResponseEntity.ok(userApps);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //this is just some boiler plate stuff for now, this is where im wanting to implement the remove users app functionality
    //@DeleteMapping("/removeApps")
    /*public ResponseEntity<Optional<UserApps>> removeUserApp(@PathVariable Long id){
        return null;
    }*/
}
