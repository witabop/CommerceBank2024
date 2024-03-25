package com.project.commercebank2024.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;
    @Column(unique = true)
    private String userName;
    private String password;
    private String role;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private String modifiedBy;

    @OneToMany(mappedBy = "userInfo")
    @JsonIgnore
    private List<AppInfo> appInfo = new ArrayList<>();
}
