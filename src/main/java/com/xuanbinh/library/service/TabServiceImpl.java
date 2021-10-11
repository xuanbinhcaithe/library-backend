package com.xuanbinh.library.service;

import com.xuanbinh.library.common.CommonUtil;
import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.CardDTO;
import com.xuanbinh.library.dto.TabDTO;
import com.xuanbinh.library.model.Tab;
import com.xuanbinh.library.repo.TabRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TabServiceImpl implements TabService {

    @Autowired
    private TabRepo tabRepo;

    @Autowired
    private CardService cardService;

    @Autowired
    private VfData vfData;

    @Override
    public Tab findById(Long id) {
        return tabRepo.findById(id).orElse(null);
    }

    @Override
    public List<TabDTO> findByBoardId(Long boardId) {
        List<TabDTO> result = tabRepo.findByBoardId(vfData, boardId);
        if (!CommonUtil.isNullOrEmpty(result)) {
            for (TabDTO dto : result) {
                List<CardDTO> cardDTOS = cardService.getByTabId(dto.getId());
                if (!CommonUtil.isNullOrEmpty(cardDTOS)) {
                    dto.setCardDTOList(cardDTOS);
                }else {
                    dto.setCardDTOList(new ArrayList<>());
                }
            }
        }
        return result;
    }

    @Override
    public void delete(Long id) {
        List<CardDTO> cardDTOS = cardService.getByTabId(id);
        List<Long> lstCardIds = cardDTOS.stream().map(x -> x.getId()).collect(Collectors.toList());
        if (!CommonUtil.isNullOrEmpty(lstCardIds)) {
            for (Long cardID : lstCardIds) {
                this.cardService.delete(cardID);
            }
        }
        tabRepo.deleteById(id);
    }

    @Override
    public Tab save(Tab tab) {
        return tabRepo.save(tab);
    }

    @Override
    public Integer getMaxOrder(Long boardId) {
        return tabRepo.getMaxOrder(vfData, boardId);
    }

    @Override
    public List<Tab> findByBoardIdOrderByTabOrder(Long boardId) {
        return tabRepo.findByBoardIdOrderByTabOrder(boardId);
    }
}
