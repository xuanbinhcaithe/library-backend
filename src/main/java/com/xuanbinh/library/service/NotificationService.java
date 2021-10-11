package com.xuanbinh.library.service;

import com.xuanbinh.library.dto.NotificationDTO;
import com.xuanbinh.library.model.Notification;

import java.util.List;

public interface NotificationService {

    Notification findById(Long id);

    NotificationDTO findDTOById(Long id);

    List<NotificationDTO> findByUserId(Long userId);

    Notification save(Notification notification);

    void deleteById(Long id);

}
