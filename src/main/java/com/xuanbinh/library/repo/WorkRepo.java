package com.xuanbinh.library.repo;


import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.WorkDTO;
import com.xuanbinh.library.model.Work;
import org.hibernate.SQLQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkRepo extends JpaRepository<Work, Long> {

    Optional<Work> findById(Long id);

    public default WorkDTO findDTOById(VfData vfData, Long id) {
        StringBuilder sql = new StringBuilder(" SELECT w.id as id, " +
                " w.name as name, " +
                " w.card_id as cardId FROM work w where w.id = :id ");
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, WorkDTO.class);
        sqlQuery.setParameter("id", id);
        sqlQuery.setMaxResults(1);
        return (WorkDTO) sqlQuery.uniqueResult();
    }

    public default List<WorkDTO> findLstDTOByCardId(VfData vfData, Long cardId) {
        StringBuilder sql = new StringBuilder(" SELECT w.id as id, " +
                " w.name as name, " +
                " w.card_id as cardId FROM work w where w.card_id = :cardId ");
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, WorkDTO.class);
        sqlQuery.setParameter("cardId", cardId);
        return sqlQuery.list();
    }

}
