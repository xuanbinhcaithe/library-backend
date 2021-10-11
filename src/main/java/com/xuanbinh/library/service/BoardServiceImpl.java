package com.xuanbinh.library.service;

import com.xuanbinh.library.common.CommonUtil;
import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.BoardDTO;
import com.xuanbinh.library.dto.CardDTO;
import com.xuanbinh.library.dto.TabDTO;
import com.xuanbinh.library.dto.UserDTO;
import com.xuanbinh.library.model.Board;
import com.xuanbinh.library.repo.BoardRepo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepo boardRepo;

    @Autowired
    VfData vfData;

    @Autowired
    UserService userService;

    @Autowired
    TabService tabService;

    @Override
    public Board findById(Long id) {
        return boardRepo.findById(id).orElse(null);
    }

    @Override
    public List<BoardDTO> getListBoardByUserId(Long userId) {
        List<BoardDTO> lsBoardDTOS = boardRepo.getListBoardByUserId(vfData, userId);
        if (!lsBoardDTOS.isEmpty()) {
            for (BoardDTO dto : lsBoardDTOS) {
                if (dto.getUsers() != null && !dto.getUsers().equals("")) {
                    String[] arrStr = dto.getUsers().split(",");
                    int size = arrStr.length;
                    dto.setCountUser(size);
                } else {
                    dto.setCountUser(0);
                }
                List<TabDTO> list = tabService.findByBoardId(dto.getId());
                if (!CommonUtil.isNullOrEmpty(list)) {
                    dto.setTabDTOList(list);
                } else {
                    dto.setTabDTOList(new ArrayList<>());
                }
            }
        }
        return lsBoardDTOS;
    }

    @Override
    public void delete(Long id) {

        boardRepo.deleteById(id);
    }

    @Override
    public Board save(Board board) {
        return boardRepo.save(board);
    }

    @Override
    public BoardDTO getBoardDTOById(Long id) {
        BoardDTO dto = boardRepo.getBoardDTOById(vfData, id);
        if (dto != null) {
            List<UserDTO> userDTOList = new ArrayList<>();
            if (dto.getUsers() != null && !dto.getUsers().trim().equals("")) {
                userDTOList = userService.findDtoByListId(dto.getUsers());
                String[] arrStr = dto.getUsers().split(",");
                int size = arrStr.length;
                dto.setCountUser(size);
            } else {
                dto.setCountUser(0);
            }
            dto.setUserDTOList(userDTOList);
            List<TabDTO> list = tabService.findByBoardId(dto.getId());
            if (!CommonUtil.isNullOrEmpty(list)) {
                dto.setTabDTOList(list);
            } else {
                dto.setTabDTOList(new ArrayList<>());
            }
        }
        return dto;
    }

    public ByteArrayInputStream exportExcel(BoardDTO boardDTO) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet("sheet1");
        Font font = workbook.createFont();
        font.setBold(true);
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        style.setFont(font);

        Row rowTitle = sheet.createRow(0);
        Cell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellValue(boardDTO.getName());
        cellTitle.setCellStyle(style);


        if (!boardDTO.getTabDTOList().isEmpty()) {
        }
        List<TabDTO> tabDTOList = boardDTO.getTabDTOList();
        int rowIndex = 4;
        Cell cell;
        Row row = sheet.createRow(rowIndex);



        for (int i = 0; i < tabDTOList.size(); i++) {
//            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, 25*256);
            cell = row.createCell(i);
            cell.setCellValue(tabDTOList.get(i).getName());
            cell.setCellStyle(style);

            List<CardDTO> cardDTOList = tabDTOList.get(i).getCardDTOList();
            if (cardDTOList.size() > 0) {
                int rowNumber = 4;
                for (int j = 0; j < cardDTOList.size(); j++) {
                    rowNumber++;
                    Row rowCard;
                    if (sheet.getRow(rowNumber) != null) {
                        rowCard = sheet.getRow(rowNumber);
                        cell = rowCard.createCell(i);
                        cell.setCellValue(cardDTOList.get(j).getName());
                    } else {
                        rowCard = sheet.createRow(rowNumber);
                        cell = rowCard.createCell(i);
                        cell.setCellValue(cardDTOList.get(j).getName());
                    }
                }
            }

        }
        workbook.write(out);

        return new ByteArrayInputStream(out.toByteArray());
    }
}
