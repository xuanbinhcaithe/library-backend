package com.xuanbinh.library.service;

import com.xuanbinh.library.dto.WorkDTO;
import com.xuanbinh.library.model.Work;

import java.util.List;

public interface WorkService {

    Work findById(Long id);

    WorkDTO findDTOById(Long id);

    List<WorkDTO> findByCardId(Long cardId);

    Work save(Work work);

    void deleteById(Long id);


}
