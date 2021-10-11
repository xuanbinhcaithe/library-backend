package com.xuanbinh.library.common;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.util.List;
import java.util.Map;

public interface VfData {

    public SessionFactory getSessionFactory();

    public <T> T get(Class<T> className, Serializable  id);

    public Session getSession();

    public String getTableName(Class className);

    public String getNameId(Class className);

    public String columnName(String input);

    public void saveOrUpdate(Object dataObj);

    public void save(Object obj);

    public void saveWithId(Object obj);

    public void delete(Object obj);

    public void deleteByIds(List<Long> arrId, Class className, String idColumn);

    public List<String>  getLstUserSequence() throws Exception;

    public void deleteByIds(Long[] arrId, Class className, String idColumn);

    public void deleteById(Long id, Class className, String idColumn);

    public <T> List<T> getAll(Class<T> className, String orderColumn);

    public <T> Page<T> listAll(Class<T> className, Pageable pageable);

    public <T> List<T> findByProperty(Class<T> className, String propertyName, Object value, String orderClause);

    public <T> List<T> findByProperties(Class<T> className, Object... pairs);

    public <T> List<T> findByIds(Class<T> tableName, String idColumn, String ids, String orderClause);

    public void flushSession();

    public void clear();

    public Query createQuery(String hql);

    public SQLQuery createSQLQuery(String sql);

    public CallableStatement prepareCall(String sql);

    public Long getSequence(String sequenceName);

    public Long getAutoIncrement(String tableName);

    public boolean duplicate(Class className, String idColumn, Long idValue, String codeColumn, String codeValue);

    public boolean hasConstraint(Class className, String idColumn, Long idValue);

    public String getOrderByClause(HttpServletRequest req, String defaultColumn, String... whiteList);

    public void setResultTransformer(SQLQuery query, Class obj);

    public List<String> getReturnAliasColumns(SQLQuery query);

    public void commitTransaction();

    public <T> List<T> list(String nativeQuery, List<Object> paramList, Class obj);

    public <T> List<T> list(String nativeQuery, Map<String, Object> mapParams, Class obj);

    public <T> T get(String nativeQuery, List<Object> paramList, Class obj);

    public <T> T get(String nativeQuery, Map<String, Object> paramList, Class obj);

    public <S extends  Object> boolean saveAllByQuery(Iterable<S> entities, Class classSave);

    public  <S extends Object> Object getPropertyValueByFieldName(S entity, Class classSave, String fieldName);

}
