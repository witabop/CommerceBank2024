package com.project.commercebank2024.controller;

import com.project.commercebank2024.domain.AppInfo;
import com.project.commercebank2024.domain.UserInfo;
import com.project.commercebank2024.repository.AppInfoRepository;
import com.project.commercebank2024.service.AppInfoService;
import com.project.commercebank2024.service.ServerInfoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/appInfo")
@CrossOrigin
public class AppInfoController {
    @Autowired
    private final AppInfoService appInfoService;
    @Autowired
    private final ServerInfoService serverInfoService;
    @Autowired
    private AppInfoRepository appInfoRepository;

    /*@CrossOrigin
    @PostMapping("/appInfo")
    public ResponseEntity<?> save(@RequestBody AppInfo appInfo){
        //Assume that admin user is already logged in to the system
        Long userId = 1L;
        return new ResponseEntity<>(appInfoService.create(appInfo, userId), HttpStatus.CREATED);
    }*/

    @DeleteMapping("/delete/{appId}")
    public ResponseEntity<String> deleteAppInfo(@PathVariable long appId) {
        AppInfo appInfo = appInfoService.findById(appId).orElse(null);
        if (appInfo == null) {
            return new ResponseEntity<>("App not found", HttpStatus.OK);
        }

        appInfoRepository.deleteById(appId);
        return new ResponseEntity<>("App deleted", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AppInfo>> getAllApps(){return new ResponseEntity<List<AppInfo>>(appInfoService.allApps(), HttpStatus.OK);}

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addAppInfo(@RequestBody Map<String, ?> appEntity) throws ParseException {
        //more datetime formatting
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdAtStr = (String) appEntity.get("createdAt");
        String modifiedAtStr = (String) appEntity.get("modifiedAt");

        try{
            //take in info inputted and create app entity based off it
            Long appInfoId = Long.valueOf(appEntity.get("appInfoId").toString());
            String appDesc = (String) appEntity.get("app_desc");
            String modifiedBy = (String) appEntity.get("modifiedBy");
            Timestamp createdAt = Timestamp.valueOf(LocalDateTime.parse(createdAtStr, DateTimeFormatter.ISO_DATE_TIME));
            Timestamp modifiedAt = Timestamp.valueOf(LocalDateTime.parse(modifiedAtStr, DateTimeFormatter.ISO_DATE_TIME));
            appInfoRepository.save(new AppInfo(appInfoId, appDesc, createdAt, modifiedAt, modifiedBy, new ArrayList<>(), new ArrayList<>()));
            return new ResponseEntity<>("App added successfully", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Server not added \n"+e, HttpStatus.OK);
        }
    }
}
