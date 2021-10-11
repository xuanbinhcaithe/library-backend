package com.xuanbinh.library.controller;


import com.xuanbinh.library.common.CommonUtil;
import com.xuanbinh.library.dto.WorkDTO;
import com.xuanbinh.library.model.Work;
import com.xuanbinh.library.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/work")
public class WorkController {

    @Autowired
    private WorkService workService;


    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        Work work = workService.findById(id);
        if (work == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(work, HttpStatus.OK);
    }

    @GetMapping("/workDTO/{id}")
    public ResponseEntity<?> findDTOById(@PathVariable("id") Long id) {
        WorkDTO workDTO = workService.findDTOById(id);
        if (workDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(workDTO, HttpStatus.OK);
    }

    @GetMapping("/cardId/{cardId}")
    public ResponseEntity<?> findByCardId(@PathVariable("cardId") Long id) {
        List<WorkDTO> list = workService.findByCardId(id);
        if (CommonUtil.isNullOrEmpty(list)) {
            return new ResponseEntity<>(new ArrayList<WorkDTO>(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Work work) {
        if (work.getId() != null) {
            Work bo = workService.findById(work.getId());
            if (bo != null) {
                bo.setName(work.getName());
                workService.save(bo);
            }
            return new ResponseEntity<>(bo, HttpStatus.OK);
        }
        return new ResponseEntity<>(workService.save(work), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        Work work = workService.findById(id);
        if (work == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        workService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
