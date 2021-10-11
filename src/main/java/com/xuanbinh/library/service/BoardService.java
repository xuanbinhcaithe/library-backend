package com.xuanbinh.library.service;

import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.BoardDTO;
import com.xuanbinh.library.model.Board;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface BoardService {
    Board findById(Long id);

    List<BoardDTO> getListBoardByUserId(Long userId);

    void delete(Long id);

    Board save(Board board);

    BoardDTO getBoardDTOById(Long id);

    ByteArrayInputStream exportExcel(BoardDTO boardDTO) throws IOException;
}
