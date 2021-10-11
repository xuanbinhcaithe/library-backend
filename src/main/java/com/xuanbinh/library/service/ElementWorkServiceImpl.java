package com.xuanbinh.library.service;

import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.ElementWorkDTO;
import com.xuanbinh.library.model.ElementWork;
import com.xuanbinh.library.repo.ElementWorkRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElementWorkServiceImpl implements ElementWorkService {

    @Autowired
    private ElementWorkRepo elementWorkRepo;

    @Autowired
    private VfData vfData;

    @Override
    public ElementWork findById(Long id) {
        return elementWorkRepo.findById(id).orElse(null);
    }

    @Override
    public ElementWorkDTO findDTOById(Long id) {
        return elementWorkRepo.findDTOById(vfData, id);
    }

    @Override
    public ElementWork save(ElementWork elementWork) {
        return elementWorkRepo.save(elementWork);
    }

    @Override
    public void delete(Long id) {
        elementWorkRepo.deleteById(id);
    }

    @Override
    public List<ElementWorkDTO> findListDTOByWorkId(Long workId) {
        return elementWorkRepo.findListDTOByWorkId(vfData, workId);
    }

    @Override
    public void deleteByWorkId(Long workId) {
        elementWorkRepo.deleteByWorkId(workId);
    }

    @Override
    public void deleteByLstWorkId(List<Long> workId) {
        elementWorkRepo.deleteByLstWorkId(workId);
    }
}
