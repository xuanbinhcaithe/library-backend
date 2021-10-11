package com.xuanbinh.library.form;

import java.util.List;

public class NotifyForm {
    private Long actionUserId;
    private Long boardId;
    private Long userId;
    private List<Long> lstUserId;

    public NotifyForm() {
    }

    public Long getActionUserId() {
        return actionUserId;
    }

    public void setActionUserId(Long actionUserId) {
        this.actionUserId = actionUserId;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getLstUserId() {
        return lstUserId;
    }

    public void setLstUserId(List<Long> lstUserId) {
        this.lstUserId = lstUserId;
    }
}
