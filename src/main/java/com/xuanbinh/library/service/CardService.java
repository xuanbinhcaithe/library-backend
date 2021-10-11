package com.xuanbinh.library.service;

import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.CardDTO;
import com.xuanbinh.library.model.Card;

import java.util.List;

public interface CardService {

    Card findById(Long id);

    CardDTO getCardDTOById(Long id);

    List<CardDTO> getByTabId(Long tabId);

    void delete(Long id);

    Card save(Card card);

    Integer getMaxOrder(Long tabId);

    List<Card> findByTabId(Long id);

}
