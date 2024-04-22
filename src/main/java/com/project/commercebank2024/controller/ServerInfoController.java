package com.project.commercebank2024.controller;

import com.project.commercebank2024.domain.ServerInfo;
import com.project.commercebank2024.domain.UserInfo;
import com.project.commercebank2024.repository.UserInfoRepository;
import com.project.commercebank2024.service.ServerInfoService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.project.commercebank2024.service.UserService;


import java.util.ArrayList;
import java.util.List;
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
    private UserService userService;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @GetMapping
    public ResponseEntity<List<ServerInfo>> getAllServers(){return new ResponseEntity<List<ServerInfo>>(serverInfoService.allServers(), HttpStatus.OK);}


    static class ServerResponse{
        @Getter
        private List<String> srcIpAddresses;
        public List<String> getSrcIpAddresses() {
            return srcIpAddresses;
        }

        public ServerResponse(List<String> srcIpAddresses){
            this.srcIpAddresses = srcIpAddresses;
        }
    }


    @GetMapping("/{u_id}")
    public ResponseEntity<?> getServersForUser(@PathVariable Long u_id){
        UserInfo user = userInfoRepository.findByuId(u_id);
        if(user == null){return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);}

        //this is probably inefficient as shit, but its 1:53 am and its the first thing i thought of, and it works so fuck it, we'll do it better later
        List<List<ServerInfo>> applications = user.getUserApps().parallelStream().map(userApps -> userApps.getAppInfo().getServerInfos()).toList();
        List<String> actualApps = new ArrayList<>();
        for(List<ServerInfo> list : applications){
            for(ServerInfo serverInfo : list){
                actualApps.add(serverInfo.getSourceIpAddress());
            }
        }

        return new ResponseEntity<>(actualApps, HttpStatus.OK);
    }
}

/*UserInfo user = userInfoRepository.findByuId(u_id);
        //this is prolly god awfully inefficient, but its 1:34 am and this was the first thing that came to mind and it works, so fuck it
        List<ServerInfo> applications = (List<ServerInfo>) user.getUserApps().parallelStream().map(userApps -> userApps.getAppInfo().getServerInfos());
        List<String> appsStrings = new ArrayList<>();
        for(ServerInfo app : applications){
            appsStrings.add(app.getSourceIpAddress());
        }
        ServerResponse serverResponse = new ServerResponse(appsStrings);
        return new ResponseEntity<>(appsStrings, HttpStatus.OK);*/