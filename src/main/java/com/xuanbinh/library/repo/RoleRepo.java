package com.xuanbinh.library.repo;

import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.model.Role;
import org.hibernate.SQLQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

    @Query("SELECT u FROM Role u where u.name = :name ")
    Role findByName(String name);


    public default List<Role> findRoleUser(VfData vfData, Long userId) {
        StringBuilder sql = new StringBuilder(" SELECT r.id as id," +
                " r.name as name FROM role r INNER JOIN user_role ur on r.id = ur.role_id WHERE ur.user_id = :userId");
        SQLQuery sqlQuery = vfData.createSQLQuery(sql.toString());
        sqlQuery.setParameter("userId", userId);
        vfData.setResultTransformer(sqlQuery, Role.class);
        List<Role> lst = sqlQuery.list();
        return sqlQuery.list();
    }


}


