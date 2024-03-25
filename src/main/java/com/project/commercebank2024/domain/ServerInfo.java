package com.project.commercebank2024.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ServerInfo {
    @Id
    private Long sid;
    private int appInfoId;
    @Column(unique = true)
    private String sourceHostname;
    @Column(unique = true)
    private String sourceIpAddress;
    private String destinationHostName;
    private String destinationIpAddress;
    private String destinationPort;
    private String ipStatus;
    private Timestamp createdAt;
    private String createBy;
    private Timestamp modifiedAt;
    private String modifiedBy;

}
