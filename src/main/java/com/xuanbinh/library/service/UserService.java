package com.xuanbinh.library.service;

import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.UserDTO;
import com.xuanbinh.library.model.User;

import java.util.List;

public interface UserService {
    UserDTO findByUsername(String username);

    List<UserDTO> getAllUser();

    User save(User user);

    UserDTO findDtoById(Long userId);

    void addRoleToUser(Long userId, String roleName);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Long countUserByUserName(String username);

    User findById(Long id);

    boolean existsByUsername(Long id, String username);

    boolean existsByEmail(Long id, String email);

    List<UserDTO> findDtoByListId(String userIds);

    List<UserDTO> getAllUserInvite(String userIds, String name);

    }
