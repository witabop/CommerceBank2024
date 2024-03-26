package com.project.commercebank2024.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserApps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userAppsUid;
    @ManyToOne
    @JoinColumn(name="uId")
    @JsonIgnore
    private UserInfo userInfo;
    @ManyToOne
    @JoinColumn(name="appInfoId")
    private AppInfo appInfo;
    private Timestamp createdAt;
    private String createdBy;
    private Timestamp modifiedAt;
    private String modifiedBy;
}
