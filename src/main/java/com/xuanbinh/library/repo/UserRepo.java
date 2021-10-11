package com.xuanbinh.library.repo;

import com.xuanbinh.library.common.CommonUtil;
import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.dto.UserDTO;
import com.xuanbinh.library.model.User;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {


    public default UserDTO findByUsername(VfData vfData, String username) {
        StringBuilder sql = new StringBuilder(" SELECT u.id as id ," +
                " u.code as code," +
                " u.username as username," +
                " u.phone as phone, " +
                " u.email as email," +
                " u.password as password ," +
                " u.is_delete as isDelete, " +
                " u.avatar_url as avatarUrl, " +
                " u.address as address " +
                " FROM user u " +
                " WHERE 1 = 1 AND ifnull(u.is_delete,0) = 0 ");
        if (!CommonUtil.isNullOrEmpty(username)) {
            sql.append(" AND u.username = :name ");
        }
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, UserDTO.class);
        if (!CommonUtil.isNullOrEmpty(username)) {
            sqlQuery.setParameter("name", username);
        }
        sqlQuery.setMaxResults(1);
        return (UserDTO) sqlQuery.uniqueResult();
    }

    public default List<UserDTO> getAllUser(VfData vfData) {
        StringBuilder sql = new StringBuilder(" SELECT u.id as id ," +
                " u.code as code," +
                " u.username as username," +
                " u.phone as phone, " +
                " u.email as email," +
                " u.password as password ," +
                " u.address as address, " +
                " u.avatar_url as avatarUrl, " +
                " u.is_delete as isDelete " +
                " FROM user u " +
                " WHERE 1 = 1 AND ifnull(u.is_delete,0) = 0 ");
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, UserDTO.class);
        return sqlQuery.list();
    }

    public default UserDTO findDtoById(VfData vfData, Long userId) {
        StringBuilder sql = new StringBuilder(" SELECT u.id as id ," +
                " u.code as code," +
                " u.username as username," +
                " u.phone as phone, " +
                " u.email as email," +
                " u.password as password ," +
                " u.address as address, " +
                " u.avatar_url as avatarUrl, " +
                " u.is_delete as isDelete " +
                " FROM user u " +
                " WHERE 1 = 1 AND ifnull(u.is_delete,0) = 0 ");
        if (userId != null && userId > 0L) {
            sql.append(" AND u.id = :userId ");
        }
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, UserDTO.class);
        if (userId != null && userId > 0L) {
            sqlQuery.setParameter("userId", userId);
        }
        sqlQuery.setMaxResults(1);
        return (UserDTO) sqlQuery.uniqueResult();
    }

    public default boolean existsByUsername(VfData vfData, String username) {
        StringBuilder sql = new StringBuilder("SELECT u.id as id  FROM user u WHERE LOWER(u.username) = LOWER(:username)");
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, UserDTO.class);
        sqlQuery.setParameter("username", username.trim());
        List result = sqlQuery.list();
        return result.size() != 0;
    }

    public default boolean existsByUsername(VfData vfData, Long id, String username) {
        StringBuilder sql = new StringBuilder("SELECT u.id as id  FROM user u WHERE LOWER(u.username) = LOWER(:username) AND u.id <> :id ");
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, UserDTO.class);
        sqlQuery.setParameter("id", id);
        sqlQuery.setParameter("username", username.trim());
        List result = sqlQuery.list();
        return result.size() != 0;
    }

    public default boolean existsByEmail(VfData vfData, String email) {
        StringBuilder sql = new StringBuilder("SELECT u.id as id  FROM User u WHERE LOWER(u.email) = LOWER(:email)");
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, UserDTO.class);
        sqlQuery.setParameter("email", email.trim());
        List result = sqlQuery.list();
        return result.size() != 0;
    }

    public default boolean existsByEmail(VfData vfData, Long id, String email) {
        StringBuilder sql = new StringBuilder("SELECT u.id as id  FROM User u WHERE LOWER(u.email) = LOWER(:email) AND u.id <> :id");
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, UserDTO.class);
        sqlQuery.setParameter("id", id);
        sqlQuery.setParameter("email", email.trim());
        List result = sqlQuery.list();
        return result.size() != 0;
    }

    public default Long countUserByUserName(VfData vfData, String username) {
        String sql = " SELECT COUNT(*) FROM User u WHERE LOWER(u.username) = LOWER(:username)";
        Query query = vfData.createQuery(sql);
        query.setParameter("username", username);
        query.setMaxResults(1);
        Long count = (Long) query.uniqueResult();
        return count;
    }

    public default List<UserDTO> findDtoByListId(VfData vfData, String userIds) {
        StringBuilder sql = new StringBuilder(" SELECT u.id as id ," +
                " u.code as code," +
                " u.username as username," +
                " u.phone as phone, " +
                " u.email as email," +
                " u.password as password ," +
                " u.address as address, " +
                " u.avatar_url as avatarUrl, " +
                " u.is_delete as isDelete " +
                " FROM user u " +
                " WHERE 1 = 1 AND ifnull(u.is_delete,0) = 0 ");
        if (userIds != null && !userIds.trim().equals("")) {
            sql.append(" AND FIND_IN_SET(u.id, :userIds) > 0");
        }
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        vfData.setResultTransformer(sqlQuery, UserDTO.class);
        if (userIds != null && !userIds.trim().equals("")) {
            sqlQuery.setParameter("userIds", userIds);
        }
        return sqlQuery.list();
    }

    Optional<User> findById(Long id);

    public default List<UserDTO> getAllUserInvite(VfData vfData, String userIds, String name) {
        StringBuilder sql = new StringBuilder(" SELECT u.id as id ," +
                " u.code as code," +
                " u.username as username," +
                " u.phone as phone, " +
                " u.email as email," +
                " u.password as password ," +
                " u.address as address, " +
                " u.avatar_url as avatarUrl, " +
                " u.is_delete as isDelete " +
                " FROM user u " +
                " WHERE 1 = 1 AND ifnull(u.is_delete,0) = 0 AND FIND_IN_SET(u.id, :userIds) < 1 ");
        if (name != null && !name.equals("")) {
            sql.append(" AND LOWER(u.username) like LOWER(:name) ");
        }
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        sqlQuery.setParameter("userIds", userIds);
        if (name != null && !name.equals("")) {
            sqlQuery.setParameter("name", "%" + name.trim() + "%");
        }
        vfData.setResultTransformer(sqlQuery, UserDTO.class);
        return sqlQuery.list();
    }

}
