package com.project.commercebank2024.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToMany(mappedBy = "appInfo",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Setter
    private List<UserApps> userApps;
    @JsonIgnore
    @OneToMany(mappedBy = "appInfo",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServerInfo> serverInfos;
}
