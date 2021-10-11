package com.xuanbinh.library.repo;


import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.ElementWorkDTO;
import com.xuanbinh.library.model.ElementWork;
import org.hibernate.SQLQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ElementWorkRepo extends JpaRepository<ElementWork, Long> {

    Optional<ElementWork> findById(Long id);

    public default ElementWorkDTO findDTOById(VfData vfData, Long id) {
        StringBuilder sql = new StringBuilder(" SELECT e.id as id, " +
                " e.name as name, " +
                " e.status as status, " +
                " e.work_id as workId FROM element_work e where e.id = :id ");
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, ElementWorkDTO.class);
        sqlQuery.setParameter("id", id);
        sqlQuery.setMaxResults(1);
        return (ElementWorkDTO) sqlQuery.uniqueResult();
    }

    public default List<ElementWorkDTO> findListDTOByWorkId(VfData vfData, Long workId) {
        StringBuilder sql = new StringBuilder(" SELECT e.id as id, " +
                " e.name as name, " +
                " e.status as status, " +
                " e.work_id as workId FROM element_work e where e.work_id = :workId ");
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, ElementWorkDTO.class);
        sqlQuery.setParameter("workId", workId);
        return sqlQuery.list();
    }

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM ElementWork e where e.workId = :workId ")
    public void deleteByWorkId(@Param("workId") Long workId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM ElementWork e where e.workId IN (:workId) ")
    public void deleteByLstWorkId(@Param("workId") List<Long> workId);


}
