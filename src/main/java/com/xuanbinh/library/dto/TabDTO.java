package com.xuanbinh.library.dto;

import java.util.List;

public class TabDTO {
    private Long id;
    private String name;
    private Integer tabOrder;
    private Long boardId;
    private List<CardDTO> cardDTOList;

    public TabDTO() {
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


    public Integer getTabOrder() {
        return tabOrder;
    }

    public void setTabOrder(Integer tabOrder) {
        this.tabOrder = tabOrder;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public List<CardDTO> getCardDTOList() {
        return cardDTOList;
    }

    public void setCardDTOList(List<CardDTO> cardDTOList) {
        this.cardDTOList = cardDTOList;
    }
}
