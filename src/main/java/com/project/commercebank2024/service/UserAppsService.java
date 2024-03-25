package com.project.commercebank2024.service;

import com.project.commercebank2024.domain.UserApps;
import com.project.commercebank2024.repository.UserAppsRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserAppsService {
    @Autowired
    private UserAppsRepository userAppsRepository;
    public UserApps create(UserApps userApps){return userAppsRepository.save(userApps);}

    public List<UserApps> allUserApps(){return userAppsRepository.findAll();}

    public Optional<UserApps> singleUserApps(Long id){return userAppsRepository.findById(id);}
}
