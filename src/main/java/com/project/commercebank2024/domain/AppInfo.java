package com.project.commercebank2024.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AppInfo {

    @Id
    private Long appInfoId;
    private String app_desc;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private String modifiedBy;

    @OneToMany(mappedBy = "appInfo")
    private List<UserApps> userApps;
    @OneToMany(mappedBy = "appInfo")
    private List<ServerInfo> serverInfos;
}
