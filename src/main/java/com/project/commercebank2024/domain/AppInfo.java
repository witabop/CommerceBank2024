package com.project.commercebank2024.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AppInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aid;
    private String desc;

    @ManyToOne
    @JoinColumn(name = "uid")
    private UserInfo userInfo;

}
