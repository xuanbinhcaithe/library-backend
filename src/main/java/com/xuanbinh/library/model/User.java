package com.xuanbinh.library.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String code;
    private String username;
    private String phone;
    private String email;
    private String password;
    private String address;
    private String avatarUrl;
    private boolean isDelete;

    public User() {
    }

    public User(Long id, String code, String username, String phone, String email, String password, boolean isDelete) {
        this.id = id;
        this.code = code;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.isDelete = isDelete;
    }

    public User(Long id, String code, String username, String phone, String email, String password, String address, String avatarUrl, boolean isDelete) {
        this.id = id;
        this.code = code;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.address = address;
        this.avatarUrl = avatarUrl;
        this.isDelete = isDelete;
    }

    public User(String username, String phone, String email, String password, boolean isDelete) {
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.isDelete = isDelete;
    }
}
