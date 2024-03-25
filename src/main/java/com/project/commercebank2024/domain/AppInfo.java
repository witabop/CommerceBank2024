package com.project.commercebank2024.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AppInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aid;
    private String app_desc;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private String modifiedBy;

    @ManyToOne
    @JoinColumn(name = "uid")
    private UserInfo userInfo;

}
