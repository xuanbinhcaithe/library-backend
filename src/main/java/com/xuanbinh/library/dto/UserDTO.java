package com.xuanbinh.library.dto;

import com.xuanbinh.library.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String code;
    private String username;
    private String phone;
    private String email;
    private String password;
    private boolean isDelete;
    private String address;
    private String avatarUrl;
    private List<Role> roles = new ArrayList<Role>();
}
