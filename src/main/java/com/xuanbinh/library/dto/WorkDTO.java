package com.xuanbinh.library.dto;

import java.util.List;

public class WorkDTO {
    private Long id;
    private String name;
    private Long cardId;
    private List<ElementWorkDTO> workDTOList;

    public WorkDTO() {
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

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public List<ElementWorkDTO> getWorkDTOList() {
        return workDTOList;
    }

    public void setWorkDTOList(List<ElementWorkDTO> workDTOList) {
        this.workDTOList = workDTOList;
    }
}
