package com.xuanbinh.library.controller;


import com.xuanbinh.library.common.CommonUtil;
import com.xuanbinh.library.dto.CardDTO;
import com.xuanbinh.library.dto.WorkDTO;
import com.xuanbinh.library.model.Card;
import com.xuanbinh.library.model.ElementWork;
import com.xuanbinh.library.service.CardService;
import com.xuanbinh.library.service.ElementWorkService;
import com.xuanbinh.library.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/card")
@CrossOrigin("*")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private WorkService workService;

    @Autowired
    private ElementWorkService elementWorkService;

    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> getDtoById(@PathVariable("id") Long id) {
        CardDTO cardDTO = cardService.getCardDTOById(id);
        if (cardDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cardDTO, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        Card card = cardService.findById(id);
        List<Card> list = cardService.findByTabId(card.getTabId()).stream().filter(x -> x.getId() != id).collect(Collectors.toList());
        if (!list.isEmpty()) {
            int i = 1;
            for (Card c : list) {
                c.setCardOrder(i);
                cardService.save(c);
                i++;
            }
        }
        List<WorkDTO> workDTOList = workService.findByCardId(id);
        List<Long> lstWorkId = new ArrayList<>();
        if (!workDTOList.isEmpty()) {
            lstWorkId = workDTOList.stream().map(x -> x.getId()).collect(Collectors.toList());
        }
        cardService.delete(id);
        workService.findByCardId(id);
        elementWorkService.deleteByLstWorkId(lstWorkId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping()
    public ResponseEntity<?> save(@RequestBody Card card) {
        Integer maxOrder = cardService.getMaxOrder(card.getTabId());
        if (maxOrder == null || maxOrder == 0) {
            card.setCardOrder(1);
        } else {
            card.setCardOrder(maxOrder + 1);
        }
        if (CommonUtil.isNullOrEmpty(card.getUsers())) {
            card.setUsers(card.getCreatedBy().toString());
        }
        card.setCreatedDate(new Date());
        return new ResponseEntity<>(cardService.save(card), HttpStatus.CREATED);
    }



    @PostMapping("/orderInTab")
    public ResponseEntity<?> changeOrderInTab(@RequestBody List<Long> listCardId) {
        if (!CommonUtil.isNullOrEmpty(listCardId)) {
            for (int i = 0; i < listCardId.size(); i++) {
                Card card = cardService.findById(listCardId.get(i));
                card.setCardOrder(i + 1);
                cardService.save(card);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/orderOtherTab/{id}")
    public ResponseEntity<?> changeOrderInTab(@PathVariable("id") Long id, @RequestBody List<Long> listCardId) {
        if (!CommonUtil.isNullOrEmpty(listCardId)) {
            for (int i = 0; i < listCardId.size(); i++) {
                Card card = cardService.findById(listCardId.get(i));
                card.setCardOrder(i + 1);
                card.setTabId(id);
                cardService.save(card);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/moveCard")
    public ResponseEntity<?> moveCard(@RequestParam Long tabId, @RequestParam Long cardId, @RequestParam int order) {
        List<Card> cardList = cardService.findByTabId(tabId);
        if (!cardList.isEmpty()) {
            List<Card> changOrderList = cardList.stream().filter(x -> x.getCardOrder() >= order).collect(Collectors.toList());
            if (!changOrderList.isEmpty()) {
                for (Card c : changOrderList) {
                    c.setCardOrder(c.getCardOrder() + 1);
                    cardService.save(c);
                }
            }
        }
        Card card = cardService.findById(cardId);
        Long tabIdPre = card.getTabId();
        List<Card> list = cardService.findByTabId(tabIdPre).stream().filter(x -> x.getId() != cardId).collect(Collectors.toList());
        if (!list.isEmpty()) {
            int i = 1;
            for (Card c : list) {
                c.setCardOrder(i);
                cardService.save(c);
                i++;
            }
        }

        card.setTabId(tabId);
        card.setCardOrder(order);
        cardService.save(card);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @PostMapping("/changeName/{id}")
    public ResponseEntity<?> changeName(@PathVariable("id") Long id, @RequestParam String name) {
        Card card = cardService.findById(id);
        if (card == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        card.setName(name);
        cardService.save(card);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/changeDes/{id}")
    public ResponseEntity<?> changeDes(@PathVariable("id") Long id, @RequestParam String description) {
        Card card = cardService.findById(id);
        if (card == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        card.setDescription(description);
        cardService.save(card);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/inviteUser/{id}")
    public ResponseEntity<?> inviteUser(@PathVariable("id") Long id, @RequestParam String userId) {
        Card card = cardService.findById(id);
        if (userId != null && !userId.equals("")) {
            card.setUsers(card.getUsers() + "," + userId.trim());
        }
        cardService.save(card);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/changeDate/{id}")
    public ResponseEntity<?> changeDate(@PathVariable("id") Long id, @RequestParam String startDate, @RequestParam String endDate, @RequestParam Integer notifyDay) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss");
        Date startD = null;
        Date endD = null;
        if (startDate != null && !"".equals(startDate)) {
            startD = formatter.parse(startDate);
        }
        if (endDate != null && !"".equals(endDate)) {
            endD = formatter.parse(endDate);
        }
        Card card = cardService.findById(id);
        card.setStartDate(startD);
        card.setEndDate(endD);
        if (notifyDay != null && !notifyDay.equals("")) {
            card.setNotifyDay(notifyDay);
        }
        cardService.save(card);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
