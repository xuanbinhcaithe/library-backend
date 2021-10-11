package com.xuanbinh.library.service;

import com.xuanbinh.library.model.Role;

import java.util.List;

public interface RoleService {
    Role findByName(String name);
    List<Role> findRoleUser(Long userId);
    Role save(Role role);
}
