package com.xuanbinh.library.repo;

import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.TabDTO;
import com.xuanbinh.library.model.Tab;
import org.hibernate.SQLQuery;
import org.hibernate.type.IntegerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface TabRepo extends JpaRepository<Tab, Long> {

    Optional<Tab> findById(Long id);

    public default List<TabDTO> findByBoardId(VfData vfData, Long boardId) {
        StringBuilder slq = new StringBuilder(" SELECT t.id as id, " +
                " t.name as name, " +
                " t.tab_order as tabOrder, " +
                " t.board_id as boardId FROM tab t WHERE t.board_id = :boardId ORDER BY t.tab_order ");

        SQLQuery sqlQuery = vfData.createSQLQuery(slq.toString());
        vfData.setResultTransformer(sqlQuery, TabDTO.class);
        sqlQuery.setParameter("boardId", boardId);
        return sqlQuery.list();
    }

    public default Integer getMaxOrder(VfData vfData, Long boardId) {
        StringBuilder sql = new StringBuilder(" SELECT MAX(t.tab_order) as maxOrder FROM tab t WHERE t.board_id = :boardId ");
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        sqlQuery.setParameter("boardId", boardId);
        sqlQuery.addScalar("maxOrder", IntegerType.INSTANCE);
        sqlQuery.setMaxResults(1);
        Integer maxOrder = (Integer) sqlQuery.uniqueResult();
        if (maxOrder == null) {
            return 0;
        }
        return maxOrder;
    }

    public default void deleteByBoardId(VfData vfData, Long boardId) {
        StringBuilder sql = new StringBuilder(" DELETE FROM  tab t WHERE t.board_id = :boardId ");
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        sqlQuery.setParameter("boardId", boardId);
        sqlQuery.executeUpdate();
    }

    List<Tab> findByBoardIdOrderByTabOrder(Long boardId);

}
