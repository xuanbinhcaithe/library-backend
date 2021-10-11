package com.xuanbinh.library.controller;

import com.xuanbinh.library.common.CommonUtil;
import com.xuanbinh.library.dto.BoardDTO;
import com.xuanbinh.library.dto.TabDTO;
import com.xuanbinh.library.dto.UserDTO;
import com.xuanbinh.library.model.Board;
import com.xuanbinh.library.model.Tab;
import com.xuanbinh.library.service.BoardService;
import com.xuanbinh.library.service.TabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private TabService tabService;

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> findById(@PathVariable("id") Long id) {
        BoardDTO boardDTO = boardService.getBoardDTOById(id);
        if (boardDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(boardDTO, HttpStatus.OK);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Board> findById(@PathVariable("id") Long id) {
//        Board board = boardService.findById(id);
//        if (board == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(board, HttpStatus.OK);
//    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<BoardDTO>> findByUserId(@PathVariable("id") Long id) {
        List<BoardDTO> boardDTOS = boardService.getListBoardByUserId(id);
        if (boardDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(boardDTOS, HttpStatus.OK);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Board> save(@PathVariable("userId") Long userId, @RequestBody Board board) {
        if (board.getId() != null) {
            Board bo = boardService.findById(board.getId());
            bo.setName(board.getName());
            bo.setDescription(board.getDescription());
            boardService.save(bo);
        } else {
            board.setCreatedBy(userId);
            board.setCreateDate(new Date());
            board.setUsers(userId.toString());
            boardService.save(board);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeBoard(@PathVariable("id") Long id) {
        List<TabDTO> list = this.tabService.findByBoardId(id);
        if (!list.isEmpty()) {
            List<Long> lstTabIds = list.stream().map(x -> x.getId()).collect(Collectors.toList());
            if (!lstTabIds.isEmpty()) {
                for (Long tabId : lstTabIds) {
                    tabService.delete(tabId);
                }
            }
        }
        boardService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/addUserToBoard/{id}")
    public ResponseEntity<List<UserDTO>> addUserToBoard(@PathVariable("id") Long id, @RequestBody String userId) {
        Board board = boardService.findById(id);
        String users = board.getUsers();
        if (users == null) {
            users = "";
        }
        users += "," + userId;
        board.setUsers(users);
        boardService.save(board);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/export/excel/{id}")
    public ResponseEntity<InputStreamResource> exportExcel(@PathVariable("id") Long id) throws IOException {
        BoardDTO boardDTO = boardService.getBoardDTOById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=boardInfo.xlsx");
        ByteArrayInputStream byteArrayInputStream = boardService.exportExcel(boardDTO);
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(byteArrayInputStream));
    }

}
