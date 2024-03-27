package com.project.commercebank2024.controller;

import com.project.commercebank2024.domain.ServerInfo;
import com.project.commercebank2024.service.ServerInfoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/servers")
@CrossOrigin
public class ServerInfoController {
    @Autowired
    private ServerInfoService serverInfoService;

    @GetMapping
    public ResponseEntity<List<ServerInfo>> getAllServers(){
        return new ResponseEntity<List<ServerInfo>>(serverInfoService.allServers(), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Optional<ServerInfo>> getSingleServer(@PathVariable Long id){
        return new ResponseEntity<Optional<ServerInfo>>(serverInfoService.singleServer(id), HttpStatus.OK);
    }
}
