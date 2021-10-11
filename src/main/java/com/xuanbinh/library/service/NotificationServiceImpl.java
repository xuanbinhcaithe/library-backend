package com.xuanbinh.library.service;


import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.NotificationDTO;
import com.xuanbinh.library.model.Notification;
import com.xuanbinh.library.repo.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    VfData vfData;

    @Override
    public Notification findById(Long id) {
        return notificationRepo.findById(id).orElse(null);
    }

    @Override
    public NotificationDTO findDTOById(Long id) {
        return notificationRepo.findDTOById(vfData, id);
    }

    @Override
    public List<NotificationDTO> findByUserId(Long userId) {
        return notificationRepo.getListByUserId(vfData, userId);
    }

    @Override
    public Notification save(Notification notification) {
        return notificationRepo.save(notification);
    }

    @Override
    public void deleteById(Long id) {
        notificationRepo.deleteById(id);
    }
}
