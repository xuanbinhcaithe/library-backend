package com.xuanbinh.library.controller;

import com.xuanbinh.library.common.CommonUtil;
import com.xuanbinh.library.dto.TabDTO;
import com.xuanbinh.library.model.Tab;
import com.xuanbinh.library.service.BoardService;
import com.xuanbinh.library.service.TabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/tab")
@CrossOrigin("*")
public class TabController {

    @Autowired
    private TabService tabService;

    @Autowired
    private BoardService boardService;

    @GetMapping("/{id}")
    public ResponseEntity<Tab> findById(@PathVariable("id") Long id) {
        Tab tab = tabService.findById(id);
        if (tab == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tab, HttpStatus.OK);
    }

    @GetMapping("/board/{id}")
    public ResponseEntity<List<TabDTO>> findByBoardId(@PathVariable("id") Long id) {
        List<TabDTO> list = tabService.findByBoardId(id);
        if (list == null || list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/board/{id}")
    public ResponseEntity<Tab> save(@PathVariable("id") Long id, @RequestBody Tab tab) {
        if (tab.getId() != null) {
            Tab bo = tabService.findById(tab.getId());
            if (bo != null) {
                bo.setName(tab.getName());
                tabService.save(bo);
            }
        } else {

            Integer maxOder = tabService.getMaxOrder(id);
            if (maxOder == null || maxOder == 0) {
                maxOder = 1;
            } else {
                maxOder += 1;
            }
            tab.setTabOrder(maxOder);
            tab.setBoardId(id);
            tabService.save(tab);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeTab(@PathVariable("id") Long id) {
        tabService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/test/{id}")
    public ResponseEntity<?> maxOrder(@PathVariable("id") Long id) {
        return new ResponseEntity<>(tabService.getMaxOrder(id), HttpStatus.OK);
    }

    @PostMapping("/moveTab")
    public ResponseEntity<?> moveTab(@RequestBody List<Long> lstTabIds) {
        if (!CommonUtil.isNullOrEmpty(lstTabIds)) {
            int i = 1;
            for (Long id : lstTabIds) {
                Tab tab = tabService.findById(id);
                tab.setTabOrder(i);
                tabService.save(tab);
                i++;
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/moveTabToBoard")
    @Transactional
    public ResponseEntity<?> moveTabToBoard(@RequestParam Long tabId, @RequestParam Long boardId) {
        if (boardId == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        Tab tab = tabService.findById(tabId);
        Long boardIdPre = tab.getBoardId();

        int maxOder = tabService.getMaxOrder(boardId);
        tab.setTabOrder(maxOder + 1);
        tab.setBoardId(boardId);
        tabService.save(tab);
        List<Tab> tabs = tabService.findByBoardIdOrderByTabOrder(boardIdPre);
        if (!tabs.isEmpty()) {
            int i = 1;
            for (Tab t : tabs) {
                t.setTabOrder(i);
                i++;
                tabService.save(t);
            }
        }
        return new ResponseEntity<>(tab, HttpStatus.OK);
    }
    

}
