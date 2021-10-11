package com.xuanbinh.library.dto;

import java.util.Date;
import java.util.List;

public class BoardDTO {
    private Long id;
    private String name;
    private String description;
    private String backgroundUrl;
    private Long createdBy;
    private Date createDate;
    private String users;
    private String createdByName;
    private String createdByAvatar;
    private Integer countUser;
    private List<UserDTO> userDTOList;
    private List<TabDTO> tabDTOList;

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

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public List<UserDTO> getUserDTOList() {
        return userDTOList;
    }

    public void setUserDTOList(List<UserDTO> userDTOList) {
        this.userDTOList = userDTOList;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public int getCountUser() {
        return countUser;
    }

    public void setCountUser(int countUser) {
        this.countUser = countUser;
    }

    public String getCreatedByAvatar() {
        return createdByAvatar;
    }

    public void setCreatedByAvatar(String createdByAvatar) {
        this.createdByAvatar = createdByAvatar;
    }

    public void setCountUser(Integer countUser) {
        this.countUser = countUser;
    }

    public List<TabDTO> getTabDTOList() {
        return tabDTOList;
    }

    public void setTabDTOList(List<TabDTO> tabDTOList) {
        this.tabDTOList = tabDTOList;
    }
}
