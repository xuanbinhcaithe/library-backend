package com.xuanbinh.library.dto;

import java.util.Date;

public class NotificationDTO {
    private Long id;

    private String content;

    private Date createdDate;

    private Boolean status;

    private Long userId;

    public NotificationDTO() {
    }

    public NotificationDTO(Long id, String content, Date createdDate, Boolean status, Long userId) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
        this.status = status;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
