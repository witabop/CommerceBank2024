package com.project.commercebank2024.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLSelect;

import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ServerInfo {
    @Id
    private Long sid;
    @ManyToOne
    @JoinColumn(name = "appInfoId")
    @JsonIgnore
    private AppInfo appInfo;
    @Column(name = "app_desc")
    private String appDesc;
    @Column(unique = true)
    private String sourceHostname;
    @Column(unique = true)
    @Getter
    private String sourceIpAddress;
    private String destinationHostName;
    private String destinationIpAddress;
    private String destinationPort;
    private Boolean ipStatus;
    private Timestamp createdAt;
    private String createBy;
    private Timestamp modifiedAt;
    private String modifiedBy;

}
