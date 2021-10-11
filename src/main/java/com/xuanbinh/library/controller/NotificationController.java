package com.xuanbinh.library.controller;


import com.xuanbinh.library.common.CommonUtil;
import com.xuanbinh.library.dto.NotificationDTO;
import com.xuanbinh.library.form.NotifyForm;
import com.xuanbinh.library.model.Board;
import com.xuanbinh.library.model.Notification;
import com.xuanbinh.library.model.User;
import com.xuanbinh.library.service.BoardService;
import com.xuanbinh.library.service.NotificationService;
import com.xuanbinh.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/notify")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private BoardService boardService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        Notification notification = notificationService.findById(id);
        if (notification == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(notification, HttpStatus.OK);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<?> findDTOById(@PathVariable("id") Long id) {
        NotificationDTO dto = notificationService.findDTOById(id);
        if (dto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    // thong bao khi them thanh vien vao board
    @PostMapping("/addUserToBoard")
    public ResponseEntity<?> notifyAddUserToBoard(@RequestBody NotifyForm form) {
        Notification bo = new Notification();
        String boardName = "";
        String userNameActionUser = "";
        if (form.getActionUserId() != null) {
            User user = userService.findById(form.getActionUserId());
            userNameActionUser = user.getUsername();
        }
        bo.setCreatedDate(new Date());
        if (form.getBoardId() != null) {
            Board board = boardService.findById(form.getBoardId());
            boardName = board.getName();
        }
        bo.setUserId(form.getUserId());
        String content = " Bạn được thêm vào bảng " + boardName + " bởi " + userNameActionUser;
        bo.setContent(content);
        notificationService.save(bo);
        return new ResponseEntity<>(bo, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> save(@RequestBody List<Long> lstUserId) {
        if (!CommonUtil.isNullOrEmpty(lstUserId)) {
            for (Long userId : lstUserId) {
                Notification bo = new Notification();
                bo.setCreatedDate(new Date());
                bo.setContent("ban da duoc them vao bang 1");
                bo.setUserId(userId);
                notificationService.save(bo);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        Notification bo = notificationService.findById(id);
        if (bo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        notificationService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
