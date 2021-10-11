package com.xuanbinh.library.repo;

import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.BoardDTO;
import com.xuanbinh.library.model.Board;
import com.xuanbinh.library.model.Role;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepo extends JpaRepository<Board, Long> {

    Optional<Board> findById(Long id);


    public default List<BoardDTO> getListBoardByUserId(VfData vfData, Long userId) {
        StringBuilder sql = new StringBuilder(" SELECT b.id as id," +
                " b.name as name," +
                " b.description as description, " +
                " b.background_url as backgroundUrl, " +
                " b.created_by as createdBy, " +
                " b.create_date as createDate," +
                " u.username as createdByName, " +
                " u.avatar_url as createdByAvatar, " +
                " b.users as users FROM board b LEFT JOIN user u on u.id = b.created_by Where 1 = 1 AND FIND_IN_SET(:userId, b.users) > 0");
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, BoardDTO.class);
        sqlQuery.setParameter("userId", userId);
        return sqlQuery.list();
    }

    public default BoardDTO getBoardDTOById(VfData vfData, Long id) {
        StringBuilder sql = new StringBuilder(" SELECT b.id as id," +
                " b.name as name," +
                " b.description as description, " +
                " b.background_url as backgroundUrl, " +
                " b.created_by as createdBy, " +
                " b.create_date as createDate, " +
                " u.username as createdByName, " +
                " u.avatar_url as createdByAvatar, " +
                " b.users as users FROM board b " +
                " LEFT JOIN user u on u.id = b.created_by " +
                " Where 1 = 1 AND b.id = :id");
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, BoardDTO.class);
        sqlQuery.setParameter("id", id);
        sqlQuery.setMaxResults(1);
        return (BoardDTO) sqlQuery.uniqueResult();
    }
}
