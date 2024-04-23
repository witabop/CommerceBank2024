package com.project.commercebank2024.service;

import com.project.commercebank2024.domain.ServerInfo;
import com.project.commercebank2024.repository.ServerInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ServerInfoService {
    @Autowired
    private ServerInfoRepository serverInfoRepository;

    public ServerInfo create(ServerInfo serverInfo){return serverInfoRepository.save(serverInfo);}
    public Optional<ServerInfo> findById(Long id){return serverInfoRepository.findById(id);}
    public List<ServerInfo> allServers(){return serverInfoRepository.findAll();}

}
