package com.project.commercebank2024.repository;

import com.project.commercebank2024.domain.ServerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerInfoRepository extends JpaRepository<ServerInfo, Long> {
    ServerInfo findBySid(Long serverid);

}
