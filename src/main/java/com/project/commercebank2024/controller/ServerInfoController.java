package com.project.commercebank2024.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.commercebank2024.domain.AppInfo;
import com.project.commercebank2024.domain.ServerInfo;
import com.project.commercebank2024.domain.UserInfo;
import com.project.commercebank2024.repository.AppInfoRepository;
import com.project.commercebank2024.repository.ServerInfoRepository;
import com.project.commercebank2024.repository.UserInfoRepository;
import com.project.commercebank2024.service.AppInfoService;
import com.project.commercebank2024.service.ServerInfoService;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.catalina.User;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.project.commercebank2024.service.UserService;


import javax.swing.text.html.Option;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/servers")
@CrossOrigin
public class ServerInfoController {
    @Autowired
    private ServerInfoService serverInfoService;
    @Autowired
    private AppInfoRepository appInfoRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private ServerInfoRepository serverInfoRepository;

    @GetMapping
    public ResponseEntity<List<ServerInfo>> getAllServers(){return new ResponseEntity<List<ServerInfo>>(serverInfoService.allServers(), HttpStatus.OK);}




    @GetMapping("/{u_id}")
    public ResponseEntity<?> getServersForUser(@PathVariable Long u_id){
        Optional<UserInfo> optionalUserInfo = userService.singleUser(u_id);
        if(!optionalUserInfo.isPresent()){return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);}
        UserInfo user = optionalUserInfo.get();
        //this is probably inefficient as shit, but its 1:53 am and its the first thing i thought of, and it works so fuck it, we'll do it better later
        List<List<ServerInfo>> applications = user.getUserApps().parallelStream().map(userApps -> userApps.getAppInfo().getServerInfos()).toList();
        List<ServerInfo> actualApps = new ArrayList<>();
        for(List<ServerInfo> list : applications){
            actualApps.addAll(list);
        }

        return new ResponseEntity<>(actualApps, HttpStatus.OK);
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addServer(@RequestBody Map<String, ?> serverEntity) throws ParseException {
        //this is here to format the datetime to a form where json wont be a bitch and whine about parsing
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdAtStr = (String) serverEntity.get("createdAt");
        String modifiedAtStr = (String) serverEntity.get("modifiedAt");

        try {
            //same idea here, json apparently really fucking hates data types other than ints or strings
            Long sid = Long.valueOf(serverEntity.get("sid").toString());
            Long appInfoId = Long.valueOf(serverEntity.get("appInfoId").toString());
            AppInfo appInfo = appInfoRepository.findByappInfoId(appInfoId);
            String appDesc = (String) serverEntity.get("appDesc");
            String sourceHostname = (String) serverEntity.get("sourceHostname");
            String sourceIpAddress = (String) serverEntity.get("sourceIpAddress");
            String destinationHostName = (String) serverEntity.get("destinationHostName");
            String destinationIpAddress = (String) serverEntity.get("destinationIpAddress");
            String destinationPort = (String) serverEntity.get("destinationPort");
            Boolean ipStatus = (Boolean) serverEntity.get("ipStatus");
            Timestamp createdAt = Timestamp.valueOf(LocalDateTime.parse(createdAtStr, DateTimeFormatter.ISO_DATE_TIME));
            String createBy = (String) serverEntity.get("createBy");
            Timestamp modifiedAt = Timestamp.valueOf(LocalDateTime.parse(modifiedAtStr, DateTimeFormatter.ISO_DATE_TIME));
            String modifiedBy = (String) serverEntity.get("modifiedBy");
            serverInfoRepository.save(new ServerInfo(sid, appInfo, appDesc, sourceHostname, sourceIpAddress, destinationHostName,
                        destinationIpAddress, destinationPort, ipStatus, createdAt, createBy, modifiedAt, modifiedBy));
            return new ResponseEntity<>("Server added successfully", HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>("Server not added " + e, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteServer(@PathVariable Long id){
        ServerInfo serverInfo = serverInfoService.findById(id).orElse(null);
        if(serverInfo == null){return new ResponseEntity<>("Server not found", HttpStatus.OK);}

        serverInfoRepository.deleteById(id);
        return new ResponseEntity<>("Server deleted successfully", HttpStatus.OK);
    }
}