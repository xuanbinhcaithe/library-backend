package com.xuanbinh.library.service;

import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.UserDTO;
import com.xuanbinh.library.model.Role;
import com.xuanbinh.library.model.User;
import com.xuanbinh.library.model.UserRole;
import com.xuanbinh.library.repo.UserRepo;
import com.xuanbinh.library.repo.UserRoleRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserServiceImp implements UserService {
    @Autowired
    private VfData vfData;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleRepo userRoleRepo;

    @Override
    public UserDTO findByUsername(String username) {
        log.info("find user by username {]", username);
        UserDTO userDTO = userRepo.findByUsername(vfData, username);
        if (userDTO != null) {
            Long userId = userDTO.getId();
            List<Role> roles = roleService.findRoleUser(userId);
            userDTO.setRoles(roles);
        }
        return userDTO;
    }

    @Override
    public List<UserDTO> getAllUser() {
        log.info("find all user");
        List<UserDTO> lst = userRepo.getAllUser(vfData);
        if (lst.isEmpty()) {
            return null;
        }
        for (UserDTO u : lst) {
            Long userId = u.getId();
            List<Role> roles = roleService.findRoleUser(userId);
            u.setRoles(roles);
        }
        return lst;
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public UserDTO findDtoById(Long userId) {
        log.info("find user by id {]", userId);
        UserDTO userDTO = userRepo.findDtoById(vfData, userId);
        if (userDTO != null) {
            List<Role> roles = roleService.findRoleUser(userId);
            userDTO.setRoles(roles);
        }
        return userDTO;
    }

    @Override
    public void addRoleToUser(Long userId, String roleName) {
        log.info("Add role {} to userId {}", roleName, userId);
        UserDTO userDTO = findDtoById(userId);
        if (userDTO != null) {
            boolean isHaveRole = false;
            if (userDTO.getRoles().size() > 0) {
                for (Role r : userDTO.getRoles()) {
                    if (r.getName().equals(roleName)) {
                        isHaveRole = true;
                        break;
                    }
                }
                if (!isHaveRole) {
                    Role role = roleService.findByName(roleName);
                    if (role != null) {
                        UserRole userRole = new UserRole();
                        userRole.setRoleId(role.getId());
                        userRole.setUserId(userId);
                        userRoleRepo.save(userRole);
                    }
                }
            }
        }

    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(vfData, username);
    }

    @Override
    public boolean existsByEmail(String username) {
        return userRepo.existsByEmail(vfData, username);
    }

    @Override
    public Long countUserByUserName(String username) {
        return userRepo.countUserByUserName(vfData, username);
    }

    @Override
    public User findById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public boolean existsByUsername(Long id, String username) {
        return userRepo.existsByUsername(vfData, id, username);
    }

    @Override
    public boolean existsByEmail(Long id, String email) {
        return userRepo.existsByEmail(vfData, id, email);
    }

    @Override
    public List<UserDTO> findDtoByListId(String userIds) {
        return userRepo.findDtoByListId(vfData, userIds);
    }

    @Override
    public List<UserDTO> getAllUserInvite(String userIds, String name) {
        List<UserDTO> lst = userRepo.getAllUserInvite(vfData, userIds, name);
        if (lst.isEmpty()) {
            return null;
        }
        for (UserDTO u : lst) {
            Long userId = u.getId();
            List<Role> roles = roleService.findRoleUser(userId);
            u.setRoles(roles);
        }
        return lst;
    }
}
