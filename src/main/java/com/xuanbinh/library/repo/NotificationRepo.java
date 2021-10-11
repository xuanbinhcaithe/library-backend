package com.xuanbinh.library.repo;


import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.NotificationDTO;
import com.xuanbinh.library.model.Notification;
import org.hibernate.SQLQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {

    Optional<Notification> findById(Long id);

    public default NotificationDTO findDTOById(VfData vfData, Long id) {
        StringBuilder sql = new StringBuilder(" SELECT n.id as id, " +
                " n.content as content," +
                " n.created_date  as createdDate, " +
                " n.status as status, " +
                " n.user_id as userId FROM notification n WHERE n.id = :id ");
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, NotificationDTO.class);
        sqlQuery.setParameter("id", id);
        sqlQuery.setMaxResults(1);
        return (NotificationDTO) sqlQuery.uniqueResult();
    }

    public default List<NotificationDTO> getListByUserId(VfData vfData, Long userId) {
        StringBuilder sql = new StringBuilder(" SELECT n.id as id, " +
                " n.content as content," +
                " n.created_date  as createdDate, " +
                " n.status as status, " +
                " n.user_id as userId FROM notification n WHERE n.user_id = :userId ");
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, NotificationDTO.class);
        sqlQuery.setParameter("userId", userId);
        return sqlQuery.list();
    }


}
