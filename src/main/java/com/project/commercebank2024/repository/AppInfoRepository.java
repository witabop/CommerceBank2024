package com.project.commercebank2024.repository;

import com.project.commercebank2024.domain.AppInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppInfoRepository extends JpaRepository<AppInfo, Long> {
}
