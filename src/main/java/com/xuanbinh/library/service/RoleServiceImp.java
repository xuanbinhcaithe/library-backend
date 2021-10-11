package com.xuanbinh.library.service;

import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.model.Role;
import com.xuanbinh.library.repo.RoleRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class RoleServiceImp implements RoleService {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private VfData vfData;

    @Override
    public Role findByName(String name) {
        return roleRepo.findByName(name);
    }

    @Override
    public List<Role> findRoleUser(Long userId) {
        return roleRepo.findRoleUser(vfData, userId);
    }

    @Override
    public Role save(Role role) {
        return roleRepo.save(role);
    }
}
