package com.xuanbinh.library.security.service;

import com.xuanbinh.library.dto.UserDTO;
import com.xuanbinh.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDTO userDTO = userService.findByUsername(s);
        if (userDTO == null) {
            throw new UsernameNotFoundException("User {} not found in database");
        }
        return UserDetailImpl.build(userDTO);
    }
}
