package com.xuanbinh.library.controller;

import com.xuanbinh.library.cloudinary.CloudinaryService;
import com.xuanbinh.library.dto.UserDTO;
import com.xuanbinh.library.exception.MessageResponse;
import com.xuanbinh.library.form.JwtResponse;
import com.xuanbinh.library.form.LoginForm;
import com.xuanbinh.library.form.RegisterForm;
import com.xuanbinh.library.model.Board;
import com.xuanbinh.library.model.Role;
import com.xuanbinh.library.model.User;
import com.xuanbinh.library.model.UserRole;
import com.xuanbinh.library.repo.UserRoleRepo;
import com.xuanbinh.library.security.jwt.JwtUtils;
import com.xuanbinh.library.security.service.UserDetailImpl;
import com.xuanbinh.library.service.RoleService;
import com.xuanbinh.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRoleRepo userRoleRepo;

    @Autowired
    CloudinaryService cloudinaryService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);

        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        List<String> roles = userDetail.getAuthorities().stream().map(x -> x.getAuthority()).collect(Collectors.toList());
        List<Role> roleList = new ArrayList<>();
        if (!roles.isEmpty()) {
            for (String str : roles) {
                Role role = roleService.findByName(str);
                if (role != null) {
                    roleList.add(role);
                }
            }
        }
        UserDTO userDTO = userService.findDtoById(userDetail.getId());
        return ResponseEntity.ok(new JwtResponse(jwt, userDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterForm registerForm) {
        if (userService.existsByUsername(registerForm.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is in use!"));
        }
        if (userService.existsByEmail(registerForm.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email is in use!"));
        }

        User bo = new User(
                registerForm.getUsername(),
                registerForm.getPhone(),
                registerForm.getEmail(),
                passwordEncoder.encode(registerForm.getPassword()),
                false
        );
        User newUser = userService.save(bo);
        UserRole userRole = new UserRole();
        userRole.setUserId(newUser.getId());
        Role role = roleService.findByName("USER");
        userRole.setRoleId(role.getId());
        userRoleRepo.save(userRole);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(userService.findDtoById(id));
    }

    @GetMapping("/allUser")
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok().body(userService.getAllUser());
    }

    @PostMapping()
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User user1 = userService.save(user);
        return new ResponseEntity<>(user1, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserInfo(@PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
        User user = userService.findById(id);
        boolean checkDuplicateUserName = userService.existsByUsername(id, userDTO.getUsername());
        if (checkDuplicateUserName) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is in use!"));
        }
        boolean checkDuplicateEmail = userService.existsByEmail(id, userDTO.getEmail());
        if (checkDuplicateEmail) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email is in use!"));
        }
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/uploadAvatar/{id}")
    @Transactional
    public ResponseEntity<?> uploadAvatar(@PathVariable("id") Long id, @RequestParam MultipartFile multipartFile) throws IOException {
        User user = userService.findById(id);
        // Xoa avatar cu
        if (user.getAvatarUrl() != null) {
            String avatarId = getString(user.getAvatarUrl());
            if (avatarId != null && !avatarId.equals("")) {
                Map result = cloudinaryService.delete(avatarId);
            }
        }
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
        if (bi == null) {
            return new ResponseEntity(new MessageResponse("File không hợp lệ!"), HttpStatus.BAD_REQUEST);
        }
        Map result = cloudinaryService.upload(multipartFile);
        String avatarUrl = (String) result.get("url");
        user.setAvatarUrl(avatarUrl);
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/removeAvatar/{id}")
    public ResponseEntity<?> removeAvatar(@PathVariable("id") Long id) throws IOException {
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map result = cloudinaryService.delete(getString(user.getAvatarUrl()));
        user.setAvatarUrl(null);
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/allUserInvite")
    public ResponseEntity<List<UserDTO>> getAllInvite(@RequestBody String userIds, @RequestParam String name) {
        return ResponseEntity.ok().body(userService.getAllUserInvite(userIds, name));
    }
    public static String getString(String str) {
        if (str != null && !str.equals("")) {

            int indexlast = str.lastIndexOf(".");
            int indexFirst = str.lastIndexOf("/");
            return str.substring(indexFirst + 1, indexlast);
        }
        return null;
    }


}
