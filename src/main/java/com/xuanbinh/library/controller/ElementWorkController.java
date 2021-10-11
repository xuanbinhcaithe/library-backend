package com.xuanbinh.library.controller;

import com.xuanbinh.library.dto.ElementWorkDTO;
import com.xuanbinh.library.model.Card;
import com.xuanbinh.library.model.ElementWork;
import com.xuanbinh.library.service.CardService;
import com.xuanbinh.library.service.ElementWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/elementWork")
public class ElementWorkController {


    @Autowired
    private ElementWorkService elementWorkService;

    @Autowired
    private CardService cardService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(elementWorkService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<?> findDTOById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(elementWorkService.findDTOById(id), HttpStatus.OK);
    }

    @GetMapping("/work/{wordId}")
    public ResponseEntity<?> findByWorkId(@PathVariable("wordId") Long wordId) {
        List<ElementWorkDTO> lst = elementWorkService.findListDTOByWorkId(wordId);
        if (lst == null || lst.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lst, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ElementWork elementWork) {
        return new ResponseEntity<>(elementWorkService.save(elementWork), HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/convertToCard")
    public ResponseEntity<?> convertToCard(@RequestParam Long tabId, @RequestParam Long userId, @RequestParam Long elementWorkId) {
        ElementWork elementWork = elementWorkService.findById(elementWorkId);
        Card card = new Card();
        if (elementWork != null) {
            card.setName(elementWork.getName());
        }
        Integer maxOrder = cardService.getMaxOrder(card.getTabId());
        if (maxOrder == null || maxOrder == 0) {
            card.setCardOrder(1);
        } else {
            card.setCardOrder(maxOrder + 1);
        }
        card.setCreatedBy(userId);
        card.setTabId(tabId);
        card.setCreatedDate(new Date());
        card.setUsers(card.getCreatedBy().toString());
        elementWorkService.delete(elementWorkId);
        cardService.save(card);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        ElementWork elementWork = elementWorkService.findById(id);
        if (elementWork == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        elementWorkService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }





}
