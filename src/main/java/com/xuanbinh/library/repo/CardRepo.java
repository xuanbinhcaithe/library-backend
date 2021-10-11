package com.xuanbinh.library.repo;

import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.CardDTO;
import com.xuanbinh.library.model.Card;
import org.hibernate.SQLQuery;
import org.hibernate.type.IntegerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepo extends JpaRepository<Card, Long> {

    Optional<Card> findById(Long id);

    public default CardDTO getCardDTOById(VfData vfData, Long id) {
        StringBuilder sql = new StringBuilder(" SELECT c.id as id, " +
                " c.name as name, " +
                " c.description as description, " +
                " c.start_date as startDate, " +
                " c.end_date as endDate, " +
                " c.created_date as createdDate, " +
                " c.created_by as createdBy, " +
                " c.background_url as backgroundUrl, " +
                " c.users as users, " +
                " c.card_order as cardOrder, " +
                " c.notify_day as notifyDay, " +
                " u.username as createdByName, " +
                " u.avatar_url as createdByAvatar, " +
                " c.tab_id as tabId FROM card c LEFT JOIN user u on c.created_by = u.id  WHERE 1 = 1 AND c.id = :id ");

        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, CardDTO.class);
        sqlQuery.setParameter("id", id);
        sqlQuery.setMaxResults(1);
        return (CardDTO) sqlQuery.uniqueResult();
    }

    public default List<CardDTO> getByTabId(VfData vfData, Long tabId) {
        StringBuilder sql = new StringBuilder(" SELECT c.id as id, " +
                " c.name as name, " +
                " c.description as description, " +
                " c.start_date as startDate, " +
                " c.end_date as endDate, " +
                " c.created_date as createdDate, " +
                " c.created_by as createdBy, " +
                " c.background_url as backgroundUrl, " +
                " c.users as users, " +
                " c.card_order as cardOrder, " +
                " c.notify_day as notifyDay, " +
                " u.username as createdByName, " +
                " u.avatar_url as createdByAvatar, " +
                " c.tab_id as tabId FROM card c LEFT JOIN user u on c.created_by = u.id WHERE 1 = 1 AND c.tab_id = :tabId  ORDER BY c.card_order");

        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, CardDTO.class);
        sqlQuery.setParameter("tabId", tabId);
        return sqlQuery.list();
    }

    public default Integer getMaxOrder(VfData vfData, Long tabId) {
        StringBuilder sql = new StringBuilder(" SELECT MAX(t.card_order) as maxOrder FROM card t WHERE t.tab_id = :tabId ");
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        sqlQuery.setParameter("tabId", tabId);
        sqlQuery.addScalar("maxOrder", IntegerType.INSTANCE);
        sqlQuery.setMaxResults(1);
        Integer maxOrder = (Integer) sqlQuery.uniqueResult();
        return maxOrder;
    }

    List<Card> findByTabIdOrderByCardOrder(Long tabId);

}
