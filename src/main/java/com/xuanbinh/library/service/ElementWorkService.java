package com.xuanbinh.library.service;

import com.xuanbinh.library.dto.ElementWorkDTO;
import com.xuanbinh.library.model.ElementWork;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ElementWorkService {

    ElementWork findById(Long id);

    ElementWorkDTO findDTOById(Long id);

    ElementWork save(ElementWork elementWork);

    void delete(Long id);

    List<ElementWorkDTO> findListDTOByWorkId(Long workId);

    void deleteByWorkId(Long workId);

     void deleteByLstWorkId(List<Long> workId);
}
