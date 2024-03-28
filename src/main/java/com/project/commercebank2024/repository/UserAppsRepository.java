package com.project.commercebank2024.repository;

import com.project.commercebank2024.domain.UserApps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAppsRepository extends JpaRepository<UserApps, Long> {
    UserApps findByuserAppsUid(Long userAppsUid);
}
