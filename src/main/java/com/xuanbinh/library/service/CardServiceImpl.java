package com.xuanbinh.library.service;

import com.xuanbinh.library.common.CommonUtil;
import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.CardDTO;
import com.xuanbinh.library.dto.UserDTO;
import com.xuanbinh.library.model.Card;
import com.xuanbinh.library.repo.CardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepo cardRepo;

    @Autowired
    private VfData vfData;

    @Autowired
    private UserService userService;

    @Override
    public Card findById(Long id) {
        return cardRepo.findById(id).orElse(null);
    }

    @Override
    public CardDTO getCardDTOById(Long id) {
        CardDTO cardDTO = cardRepo.getCardDTOById(vfData, id);
        if (cardDTO.getUsers() != null && !cardDTO.getUsers().equals("")) {
            List<UserDTO> list = userService.findDtoByListId(cardDTO.getUsers());
            if (!list.isEmpty()) {
                cardDTO.setUserDTOList(list);
            }
        }
        return cardDTO;
    }

    @Override
    public List<CardDTO> getByTabId(Long tabId) {
        List<CardDTO> result = cardRepo.getByTabId(vfData, tabId);
        if (!CommonUtil.isNullOrEmpty(result)) {
            for (CardDTO cardDTO : result) {
                if (!CommonUtil.isNullOrEmpty(cardDTO.getUsers())) {
                    List<UserDTO> list = userService.findDtoByListId(cardDTO.getUsers());
                    if (!list.isEmpty()) {
                        cardDTO.setUserDTOList(list);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void delete(Long id) {
        cardRepo.deleteById(id);
    }

    @Override
    public Card save(Card card) {
        return cardRepo.save(card);
    }

    @Override
    public Integer getMaxOrder(Long tabId) {
        return cardRepo.getMaxOrder(vfData, tabId);
    }

    @Override
    public List<Card> findByTabId(Long id) {
        return cardRepo.findByTabIdOrderByCardOrder(id);
    }

}
