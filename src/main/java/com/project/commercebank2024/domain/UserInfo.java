package com.project.commercebank2024.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Long uId;
    @Column(unique = true)
    private String userName;
    private String password;
    private String role;
    private Timestamp createdAt;
    private String createdBy;
    private Timestamp modifiedAt;
    private String modifiedBy;

    @OneToMany(mappedBy = "userInfo")
    @Setter
    @JsonIgnore
    private List<UserApps> userApps = new ArrayList<>();

    public void addUserApps(UserApps appToAdd){
        userApps.add(appToAdd);
    }

}
