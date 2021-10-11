package com.xuanbinh.library.service;

import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.TabDTO;
import com.xuanbinh.library.model.Tab;

import java.util.List;

public interface TabService {
    Tab findById(Long id);

    List<TabDTO> findByBoardId(Long boardId);

    void delete(Long id);

    Tab save(Tab tab);

    Integer getMaxOrder(Long boardId);
    List<Tab> findByBoardIdOrderByTabOrder(Long boardId);

}
