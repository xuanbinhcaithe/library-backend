package com.xuanbinh.library.dto;

import java.util.Date;
import java.util.List;

public class CardDTO {
    private Long id;

    private String name;

    private String description;

    private Date startDate;

    private Date endDate;

    private Date createdDate;

    private String createdByName;

    private String createdByAvatar;

    private String backgroundUrl;

    private String users;

    private Long tabId;

    private Integer cardOrder;

    private List<UserDTO> userDTOList;

    private Integer notifyDay;

    public CardDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getCreatedByAvatar() {
        return createdByAvatar;
    }

    public void setCreatedByAvatar(String createdByAvatar) {
        this.createdByAvatar = createdByAvatar;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public Long getTabId() {
        return tabId;
    }

    public void setTabId(Long tabId) {
        this.tabId = tabId;
    }

    public List<UserDTO> getUserDTOList() {
        return userDTOList;
    }

    public void setUserDTOList(List<UserDTO> userDTOList) {
        this.userDTOList = userDTOList;
    }

    public Integer getCardOrder() {
        return cardOrder;
    }

    public void setCardOrder(Integer cardOrder) {
        this.cardOrder = cardOrder;
    }

    public Integer getNotifyDay() {
        return notifyDay;
    }

    public void setNotifyDay(Integer notifyDay) {
        this.notifyDay = notifyDay;
    }
}
