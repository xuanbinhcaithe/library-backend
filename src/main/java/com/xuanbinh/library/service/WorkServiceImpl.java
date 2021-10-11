package com.xuanbinh.library.service;


import com.xuanbinh.library.common.CommonUtil;
import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.ElementWorkDTO;
import com.xuanbinh.library.dto.WorkDTO;
import com.xuanbinh.library.model.ElementWork;
import com.xuanbinh.library.model.Work;
import com.xuanbinh.library.repo.WorkRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkServiceImpl implements WorkService {

    @Autowired
    private WorkRepo workRepo;

    @Autowired
    VfData vfData;

    @Autowired
    private ElementWorkService elementWorkService;

    @Override
    public Work findById(Long id) {
        return workRepo.findById(id).orElse(null);
    }

    @Override
    public WorkDTO findDTOById(Long id) {
        WorkDTO workDTO = workRepo.findDTOById(vfData, id);
        List<ElementWorkDTO> list = elementWorkService.findListDTOByWorkId(workDTO.getId());
        if (!CommonUtil.isNullOrEmpty(list)) {
            workDTO.setWorkDTOList(list);
        } else {
            workDTO.setWorkDTOList(new ArrayList<>());
        }
        return workDTO;

    }

    @Override
    public List<WorkDTO> findByCardId(Long cardId) {
        List<WorkDTO> workDTOList = workRepo.findLstDTOByCardId(vfData, cardId);
        if (!CommonUtil.isNullOrEmpty(workDTOList)) {
            for (WorkDTO workDTO : workDTOList) {
                List<ElementWorkDTO> list = elementWorkService.findListDTOByWorkId(workDTO.getId());
                if (!CommonUtil.isNullOrEmpty(list)) {
                    workDTO.setWorkDTOList(list);
                } else {
                    workDTO.setWorkDTOList(new ArrayList<>());
                }
            }
        }
        return workDTOList;
    }

    @Override
    public Work save(Work work) {
        return workRepo.save(work);
    }

    @Override
    public void deleteById(Long id) {
        workRepo.deleteById(id);
    }
}
