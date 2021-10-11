package com.xuanbinh.library.common;

import org.hibernate.*;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class VfDataImpl implements VfData {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private HttpServletRequest req;

    private final static Logger LOGGER = LoggerFactory.getLogger(VfDataImpl.class);


    @Override
    public SessionFactory getSessionFactory() {
        return entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
    }

    @Override
    public <T> T get(Class<T> className, Serializable id) {
        return (T) getSession().get(className, id);
    }

    @Override
    public Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public String getTableName(Class className) {
        Class<?> modelClass = className;
        Table table = modelClass.getAnnotation(Table.class);
        return table.name();
    }

    @Override
    public String getNameId(Class className) {
        String nameId = "";
        for (Method m : className.getDeclaredMethods()) {
            Id a = m.getAnnotation(Id.class);
            Column c = m.getAnnotation(Column.class);
            if (a != null && c != null) {
                nameId = c.name();
                break;
            }
        }

        return columnName(nameId);
    }

    @Override
    public String columnName(String input) {
        try {
            String columnName = "";
            if (input != null && input.length() > 0) {
                for (int i = 0; i < input.length(); i++) {
                    Character c = input.charAt(i);
                    if (c == '_') {
                        break;
                    }
                    columnName += Character.toUpperCase(input.charAt(i));
                }
            }
            return columnName;
        } catch (Exception e) {
            LOGGER.error("error", e)
            ;
            return "ABC";
        }
    }

    @Override
    public void saveOrUpdate(Object dataObj) {
        getSession().saveOrUpdate(dataObj);
    }

    @Override
    public void save(Object obj) {
        getSession().save(obj);
    }

    @Override
    public void saveWithId(Object obj) {
        getSession().replicate(obj, ReplicationMode.EXCEPTION);
    }

    @Override
    public void delete(Object obj) {
        getSession().delete(obj);
    }

    @Override
    public void deleteByIds(List<Long> arrId, Class className, String idColumn) {
        if (arrId != null && !arrId.isEmpty()) {
            String sql = "DELETE FROM " + className.getName() + "t WHERE t." + idColumn + " IN (:arrId)";
            Query query = getSession().createQuery(sql);
            query.setParameter("arrId", arrId);
            query.executeUpdate();
        }
    }

    @Override
    public List<String> getLstUserSequence() throws Exception {
        String sql = "SELECT lower(sequence_name) FROM USER_SEQUENCES";
        SQLQuery query = getSession().createSQLQuery(sql);
        return query.list();
    }

    @Override
    public void deleteByIds(Long[] arrId, Class className, String idColumn) {
        if (arrId != null && arrId.length > 0) {
            String sql = "DELETE FROM " + className.getName() + "t WHERE t." + idColumn + " IN (:arrId)";
            Query query = getSession().createQuery(sql);
            query.setParameter("arrId", arrId);
            query.executeUpdate();
        }
    }

    @Override
    public void deleteById(Long id, Class className, String idColumn) {
        if (id != null) {
            String sql = "DELETE FROM " + className.getName() + " t WHERE t." + idColumn + " = :id";
            Query query = getSession().createQuery(sql);
            query.setParameter("id", id);
            query.executeUpdate();
        }
    }

    @Override
    public <T> List<T> getAll(Class<T> className, String orderColumn) {
        String sql = " FROM " + className.getName() + " t ORDER BY t." + orderColumn;
        Query query = getSession().createQuery(sql);
        return query.list();
    }

    @Override
    public <T> Page<T> listAll(Class<T> className, Pageable pageable) {
        String sql = " FROM " + className.getName() + " t";
        String countSql = "SELECT count(*) ";
        sql = QueryUtils.applySorting(sql, pageable.getSort());
        Query objQuery = getSession().createQuery(sql);
        Query countQuery = getSession().createQuery(countSql + sql);
        objQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        objQuery.setMaxResults(pageable.getPageSize());
        return new PageImpl<T>(objQuery.list(), pageable, (long) countQuery.uniqueResult());
    }

    @Override
    public <T> List<T> findByProperty(Class<T> className, String propertyName, Object value, String orderClause) {
        String sql = "";
        if (value == null) {
            sql = " FROM " + className.getName() + " t WHERE t." + propertyName + "IS NULL ORDER BY t." + orderClause;
        } else {
            sql = " FROM " + className.getName() + " t WHERE t." + propertyName + " = ? " + " ORDER BY t." + orderClause;
        }
        Query query = getSession().createQuery(sql);
        if (value != null) {
            query.setParameter(0, value);
        }
        return query.list();
    }

    @Override
    public <T> List<T> findByProperties(Class<T> className, Object... pairs) {
        StringBuilder sql = new StringBuilder(" FROM " + className.getName() + " t WHERE 1 = 1 ");
        List<Object> lstParams = new ArrayList<Object>();

        if (pairs != null && pairs.length % 2 == 0) {
            int index = 0;
            for (Object obj : pairs) {
                if (index % 2 == 0) {
                    sql.append(" AND t." + (String) obj + " = ?");
                } else {
                    lstParams.add(obj);
                }
                index++;
            }
        }
        Query query = getSession().createQuery(sql.toString());
        if (lstParams.size() > 0) {
            for (int i = 0; i < lstParams.size(); i++) {
                query.setParameter(i, lstParams.get(i));
            }
        }
        return query.list();
    }

    @Override
    public <T> List<T> findByIds(Class<T> tableName, String idColumn, String ids, String orderClause) {
        StringBuilder sql = new StringBuilder(" FROM " + tableName.getName() + " t ");
        List<Long> lstIds = new ArrayList<>();
        if (!ids.isEmpty()) {
            lstIds = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            if (!lstIds.isEmpty()) {
                sql.append(" WHERE t.").append(idColumn).append(" IN (:lstIds) ORDER BY t.").append(orderClause);
            }
        }
        Query query = getSession().createQuery(sql.toString());
        if (lstIds.size() > 0) {
            query.setParameter("lstIds", lstIds);
        }
        return query.list();
    }

    @Override
    public void flushSession() {
        getSession().flush();
    }

    @Override
    public void clear() {
        getSession().clear();
    }

    @Override
    public Query createQuery(String hql) {
        return getSession().createQuery(hql);
    }

    @Override
    public SQLQuery createSQLQuery(String sql) {
        return getSession().createSQLQuery(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) {
        return null;
    }

    @Override
    public Long getSequence(String sequenceName) {
        String sql = "SELECT " + sequenceName + ".nextVal" + " FROM dual";
        Query query = getSession().createSQLQuery(sql);
        BigDecimal bigDecimal = (BigDecimal) query.uniqueResult();
        return bigDecimal.longValue();
    }

    @Override
    public Long getAutoIncrement(String tableName) {
        String sql = " SELECT AUTO_INCREMENT FROM information_schema.TABLES ";
        return null;
    }

    @Override
    public boolean duplicate(Class className, String idColumn, Long idValue, String codeColumn, String codeValue) {
        String sql = " SELECT COUNT(*) FROM " + className.getName() + "t WHERE LOWER(t." + codeColumn + ") = ?";
        if (idValue != null) {
            sql += "AND t." + idColumn + " != ?";
        }
        Query query = createQuery(sql);
        query.setParameter(0, codeValue.trim().toLowerCase());
        if (idValue != null) {
            query.setParameter(1, idValue);
        }
        query.setMaxResults(1);
        Long count = (Long) query.uniqueResult();
        return count > 0;
    }

    @Override
    public boolean hasConstraint(Class className, String idColumn, Long idValue) {
        String hql = " SELECT COUNT(*) FROM " + className.getName() + "t WHERE t." + idColumn + " = ?";
        Query query = createQuery(hql);
        query.setParameter(0, idValue);
        query.setMaxResults(1);
        Long count = (Long) query.uniqueResult();
        return count > 0;
    }

    @Override
    public String getOrderByClause(HttpServletRequest req, String defaultColumn, String... whiteList) {
        String inputColumn = req.getParameter("sort");
        String direction = req.getParameter("dir");
        String finalColumn = null;
        if (inputColumn == null || inputColumn.isEmpty()) {
            finalColumn = defaultColumn;
        } else {
            if (whiteList == null || whiteList.length == 0) {
                finalColumn = defaultColumn;
            } else {
                for (String s : whiteList) {
                    if (inputColumn.equals(s)) {
                        finalColumn = inputColumn;
                        break;
                    }
                }
                if (finalColumn == null) {
                    finalColumn = defaultColumn;
                }
            }
        }
        return " ODER BY " + finalColumn + (direction == null || direction.isEmpty() ? "" : " " + direction);
    }

    @Override
    public void setResultTransformer(SQLQuery query, Class obj) {
        Field[] fields = obj.getDeclaredFields();
        Map<String, String> maFields = new HashMap<>();
        for (Field field : fields) {
            maFields.put(field.getName(), field.getType().toString());
        }
        List<String> aliasColumns = getReturnAliasColumns(query);
        for (String aliasColumn : aliasColumns) {
            String dataType = maFields.get(aliasColumn);
            if (dataType == null) {
                LOGGER.debug(aliasColumn + " is not dèined");
            } else {
                Type hbmType = null;
                if ("class java.lang.Long".equals(dataType)) {
                    hbmType = LongType.INSTANCE;
                } else if ("class java.lang.Integer".equals(dataType)) {
                    hbmType = IntegerType.INSTANCE;
                } else if ("class java.lang.Double".equals(dataType)) {
                    hbmType = DoubleType.INSTANCE;
                } else if ("class java.lang.String".equals(dataType)) {
                    hbmType = StringType.INSTANCE;
                } else if ("class java.lang.Boolean".equals(dataType)) {
                    hbmType = BooleanType.INSTANCE;
                } else if ("class java.util.Date".equals(dataType)) {
                    hbmType = TimestampType.INSTANCE;
                } else if ("class java.math.BigDecimal".equals(dataType)) {
                    hbmType = new BigDecimalType();
                }
                if (hbmType == null) {
                    LOGGER.debug(dataType + " is not supported");
                } else {
                    query.addScalar(aliasColumn, hbmType);
                }
            }
        }
        query.setResultTransformer(Transformers.aliasToBean(obj));
    }

    @Override
    public List<String> getReturnAliasColumns(SQLQuery query) {
        List<String> aliasColumn = new ArrayList<>();
        String sqlQuery = query.getQueryString();
        sqlQuery = sqlQuery.replace("\n", " ");
        sqlQuery = sqlQuery.replace("\t", " ");
        int numOfRightPythis = 0;
        int startPythis = -1;
        int endPythis = 0;
        boolean hasRightPythis = true;
        while (hasRightPythis) {
            char[] arrStr = sqlQuery.toCharArray();
            hasRightPythis = false;
            int idx = 0;
            for (char c : arrStr) {
                if (idx > startPythis) {
                    if ("(".equalsIgnoreCase(String.valueOf(c))) {
                        if (numOfRightPythis == 0) {
                            startPythis = idx;
                        }
                        numOfRightPythis++;
                    } else if (")".equalsIgnoreCase(String.valueOf(c))) {
                        if (numOfRightPythis > 0) {
                            numOfRightPythis--;
                            if (numOfRightPythis == 0) {
                                endPythis = idx;
                                break;
                            }
                        }
                    }
                }
                idx++;
            }
            if (endPythis > 0) {
                sqlQuery = sqlQuery.substring(0, startPythis) + " # " + sqlQuery.substring(endPythis + 1);
                hasRightPythis = true;
                endPythis = 0;
            }
        }

        String arrStr[] = sqlQuery.substring(0, sqlQuery.toUpperCase().indexOf(" FROM ")).split(",");
        for (String str : arrStr) {
            String[] temp = str.trim().split(" ");
            String alias = temp[temp.length - 1].trim();
            if (alias.contains(".")) {
                alias = alias.substring(alias.lastIndexOf(".") + 1).trim();
            }
            if (alias.contains(",")) {
                alias = alias.substring(alias.lastIndexOf(",") + 1).trim();
            }
            if (alias.contains("`")) {
                alias = alias.replace("`", "");
            }
            if (!aliasColumn.contains(alias)) {
                aliasColumn.add(alias);
            }
        }
        return aliasColumn;
    }

    @Override
    public void commitTransaction() {
        Session session = getSession();
        if (session != null && session.isOpen()) {
            session.getTransaction().commit();
        }
    }

    @Override
    public <T> List<T> list(String nativeQuery, List<Object> paramList, Class obj) {
        SQLQuery sqlQuery = createSQLQuery(nativeQuery);
        setResultTransformer(sqlQuery, obj);
        if (paramList != null && !paramList.isEmpty()) {
            for (int i = 0; i < paramList.size(); i++) {
                sqlQuery.setParameter(i, paramList.get(i));
            }
        }
        return sqlQuery.list();
    }

    @Override
    public <T> List<T> list(String nativeQuery, Map<String, Object> mapParams, Class obj) {
        SQLQuery sqlQuery = createSQLQuery(nativeQuery);
        setResultTransformer(sqlQuery, obj);
        if (mapParams != null && !mapParams.isEmpty()) {
            sqlQuery.setProperties(mapParams);
        }

        return sqlQuery.list();
    }

    @Override
    public <T> T get(String nativeQuery, List<Object> paramList, Class obj) {
        SQLQuery sqlQuery = createSQLQuery(nativeQuery);
        setResultTransformer(sqlQuery, obj);
        if (paramList != null && paramList.size() > 0) {
            for (int i = 0; i < paramList.size(); i++) {
                sqlQuery.setParameter(i, paramList.get(i));
            }
        }
        sqlQuery.setMaxResults(1);
        return (T) sqlQuery.uniqueResult();
    }

    @Override
    public <T> T get(String nativeQuery, Map<String, Object> paramList, Class obj) {
        SQLQuery query = createSQLQuery(nativeQuery);
        setResultTransformer(query, obj);
        if (paramList != null && !paramList.isEmpty()) {
            query.setProperties(paramList);
        }
        query.setMaxResults(1);
        return (T) query.uniqueResult();
    }

    @Override
    public <S extends Object> boolean saveAllByQuery(Iterable<S> entities, Class classSave) {
        return false;
    }

    @Override
    public <S> Object getPropertyValueByFieldName(S entity, Class classSave, String fieldName) {
        return null;
    }

}
