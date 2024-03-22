package com.project.commercebank2024.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String userId;
    private String password;
    private String role;

    @OneToMany(mappedBy = "userInfo")
    @JsonIgnore
    private List<AppInfo> appInfo = new ArrayList<>();
}
