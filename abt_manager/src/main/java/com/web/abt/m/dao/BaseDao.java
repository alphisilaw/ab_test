package com.web.abt.m.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetMetaDataImpl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.coola.jutil.data.DataPage;
import com.coola.jutil.sql.DBEngine;
import com.coola.jutil.sql.DBFactory;
import com.coola.jutil.sql.ResultPage;
import com.web.abt.m.model.BaseModel;

@SuppressWarnings({"unchecked", "deprecation"})
public class BaseDao<T extends BaseModel> {
    /**
     * 日志处理类实例
     */
    public static Log logger = LogFactory.getLog(BaseDao.class);
    /**
     * 只读DB引擎
     */
    public static DBEngine readDBEngine = DBFactory.getDBEngine("abt_read");
    /**
     * 写DB引擎
     */
    public static DBEngine writeDBEngine = DBFactory.getDBEngine("abt_write");

    /**
     * 常链接的数据库名称
     */
    public static String LOCAL_WRITE_POOL = "abt_write";
    
    public static String LOCAL_READ_POOL = "abt_read";

    /**
     * 主表名
     */
    private String tableName;

    /**
     * 主表ID
     */
    private String tableKeyId;

    /**
     * 具体的实现类
     */
    private Class<T> entityClass;

	public BaseDao(String tableName, String tableKeyId) {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class<T>) params[0];
        this.tableName = tableName;
        this.tableKeyId = tableKeyId;
    }

    /**
     * 获得model集合
     * 
     * @param sql
     *            查询语句
     * @return 返回Model集合
     */
	protected List<T> queryModelList(String sql) {
        List<T> modelList = new ArrayList<T>();
        try {
			ResultSet rs = readDBEngine.executeQuery(sql);
            while (rs.next()) {
                modelList.add((T) entityClass.newInstance().getModelByRs(rs));
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return modelList;
    }

    /**
     * 获得model集合
     * 
     * @param sql
     *            查询语句
     * @return 返回Model集合
     */
    protected List<T> queryModelListByBackRead(String sql) {
        List<T> modelList = new ArrayList<T>();
        try {
            ResultSet rs = readDBEngine.executeQuery(sql);
            while (rs.next()) {
                modelList.add((T) entityClass.newInstance().getModelByRs(rs));
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return modelList;
    }

    /**
     * 获得model集合
     * 
     * @param sql
     *            查询语句
     * @param objs
     *            参数集
     * @return 返回Model集合
     */
    protected List<T> queryModelList(String sql, Object... paramObjs) {
        List<T> modelList = new ArrayList<T>();
        try {
            ResultSet rs = readDBEngine.executeQuery(sql, paramObjs);
            while (rs.next()) {
                modelList.add((T) entityClass.newInstance().getModelByRs(rs));
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return modelList;
    }

    /**
     * 获得model集合
     * 
     * @param sql
     *            查询语句
     * @param objs
     *            参数集
     * @return 返回Model集合
     */
    protected List<T> queryModelListByWriteDB(String sql, Object[] paramObjs) {
        List<T> modelList = new ArrayList<T>();
        try {
            ResultSet rs = writeDBEngine.executeQuery(sql, paramObjs);
            while (rs.next()) {
                modelList.add((T) entityClass.newInstance().getModelByRs(rs));
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return modelList;
    }

    /**
     * 获得model集合(从备份库查询)
     * 
     * @param sql
     *            查询语句
     * @param objs
     *            参数集
     * @return 返回Model集合
     */
    protected List<T> queryModelListByBackRead(String sql, Object[] paramObjs) {
        List<T> modelList = new ArrayList<T>();
        try {
            ResultSet rs = readDBEngine.executeQuery(sql, paramObjs);
            while (rs.next()) {
                modelList.add((T) entityClass.newInstance().getModelByRs(rs));
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return modelList;
    }

    /**
     * 获得model
     * 
     * @param sql
     *            查询语句
     * @param objs
     *            参数集
     * @return 返回Model集合
     */
    protected T queryModel(String sql, Object[] paramObjs) {
        T t = null;
        try {
            ResultSet rs = readDBEngine.executeQuery(sql, paramObjs);
            if (rs.next()) {
                t = (T) entityClass.newInstance().getModelByRs(rs);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 获得model
     * 
     * @param sql
     *            查询语句
     * @param objs
     *            参数集
     * @return 返回Model集合
     */
    protected T queryModelByWriteDBEngine(String sql, Object[] paramObjs) {
        T t = null;
        try {
            ResultSet rs = writeDBEngine.executeQuery(sql, paramObjs);
            if (rs.next()) {
                t = (T) entityClass.newInstance().getModelByRs(rs);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 获得model
     * 
     * @param sql
     *            查询语句
     * @param objs
     *            参数集
     * @return 返回Model集合
     */
    protected T queryModelByBackRead(String sql, Object[] paramObjs) {
        T t = null;
        try {
			ResultSet rs = readDBEngine.executeQuery(sql, paramObjs);
            if (rs.next()) {
                t = (T) entityClass.newInstance().getModelByRs(rs);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 按主键查询model
     * 
     * @param id
     *            id
     * @return model
     */
    public T findByPrimaryKey(Integer id) {
        String sql = new StringBuilder()
        	.append("SELECT * FROM ")
        	.append(tableName)
        	.append(" WHERE ")
        	.append(tableKeyId)
        	.append(" = ?").toString();
        Object[] paramObjs = {id};
        List<T> modelList = this.queryModelList(sql, paramObjs);
        if (modelList != null && modelList.size() > 0) {
            return modelList.get(0);
        }
        return null;
    }

    /**
     * 按主键查询model
     * 
     * @param id
     *            id
     * @return model
     */
    public T findByPrimaryKeyByWriteDB(Integer id) {
        String sql = new StringBuilder()
    	.append("SELECT * FROM ")
    	.append(tableName)
    	.append(" WHERE ")
    	.append(tableKeyId)
    	.append(" = ?").toString();
        Object[] paramObjs = {id};
        return queryModelByWriteDBEngine(sql, paramObjs);
    }

    /**
     * 按主键查询model
     * 
     * @param id
     *            id
     * @return model
     */
    public T findByPrimaryKeyByBackRead(Integer id) {
        String sql = new StringBuilder()
    	.append("SELECT * FROM ")
    	.append(tableName)
    	.append(" WHERE ")
    	.append(tableKeyId)
    	.append(" = ?")
    	.toString();
        Object[] paramObjs = {id};
        return queryModelByBackRead(sql, paramObjs);
    }

    /**
     * 按主键删除一条数据
     * 
     * @param id
     *            id
     * @return 删除成功返回true,否则返回false
     */
    public boolean deleteByPrimaryKey(Integer id) {
        String sql = new StringBuilder()
        	.append("DELETE FROM ")
        	.append(tableName)
        	.append(" WHERE ")
        	.append(tableKeyId)
        	.append(" = ?")
        	.toString();
        Object[] paramObjs = {id};
        try {
            return writeDBEngine.executeUpdate(sql, paramObjs) > 0;
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 获得model集合
     * 
     * @return 返回Model集合
     */
    public List<T> findAll() {
        String sql = "SELECT * FROM " + tableName;
        return queryModelList(sql);
    }

    /**
     * 获分页
     * 
     * @param sql
     *            查询条件
     * @param pageSize
     *            分页大小
     * @param pageNo
     *            页码
     * @return 返回分页对象
     */
    protected DataPage<T> findPage(String sql, int pageSize, int pageNo) {
        CachedRowSet rs = null;
        DataPage<T> resultList = null;
        try {
            ResultPage page = readDBEngine.queryPage(sql, pageSize, pageNo);
            rs = page.getRecord();
            List<T> list = new ArrayList<T>();
            while (rs.next()) {
                list.add((T) entityClass.newInstance().getModelByRs(rs));
            }
            if (list.size() > 0) {
                int totalRecord = getTotalRecords(sql);
                resultList = new DataPage<T>(list, totalRecord, pageSize, pageNo);
            }else{
            	resultList = new DataPage<T>(list, 0, pageSize, pageNo);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * 获分页
     * 
     * @param sql
     *            查询条件
     * @param pageSize
     *            分页大小
     * @param pageNo
     *            页码
     * @return 返回分页对象
     */
    protected DataPage<T> findPageByBackRead(String sql, int pageSize, int pageNo) {
        CachedRowSet rs = null;
        DataPage<T> resultList = null;
        try {
            ResultPage page = readDBEngine.queryPage(sql, pageSize, pageNo);
            rs = page.getRecord();
            List<T> list = new ArrayList<T>();
            while (rs.next()) {
                list.add((T) entityClass.newInstance().getModelByRs(rs));
            }
            if (list.size() > 0) {
                int totalRecord = getTotalRecordsByBackRead(sql);
                resultList = new DataPage<T>(list, totalRecord, pageSize, pageNo);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * 获取总的记录条数
     * 
     * @param sql
     * @return
     */
    public int getTotalRecords(String sql) {
        int count = 0;
        if (StringUtils.isBlank(sql)) {
            sql = "SELECT count(1) as totalCount FROM " + tableName;
        } else {
            logger.info("getTotalRecords before:sql=" + sql);
            sql = sql.replace("where", "WHERE")
                     .replace("from", "FROM")
                     .replace("order", "ORDER")
                     .replace("limit", "LIMIT");
            sql = sql.substring(sql.indexOf("FROM"), sql.length());
            if (sql.contains("ORDER")) {
                sql = sql.substring(0, sql.indexOf("ORDER"));
            }
            if (sql.contains("LIMIT")) {
                sql = sql.substring(0, sql.indexOf("LIMIT"));
            }
            sql = "SELECT  count(1) as totalCount " + sql;
            logger.info("getTotalRecords after:sql=" + sql);
        }
        try {
            ResultSet rs = readDBEngine.executeQuery(sql);
            if (rs.next()) {
                count = rs.getInt(getColIdxByLabelName("totalCount", rs));
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 获取总的记录条数
     * 
     * @param sql
     * @return
     */
    public int getTotalRecordsByBackRead(String sql) {
        int count = 0;
        if (StringUtils.isBlank(sql)) {
            sql = "SELECT count(1) as totalCount FROM " + tableName;
        } else {
            logger.info("getTotalRecords before:sql=" + sql);
            sql = sql.replace("where", "WHERE")
                     .replace("from", "FROM")
                     .replace("order", "ORDER")
                     .replace("limit", "LIMIT");
            sql = sql.substring(sql.indexOf("FROM"), sql.length());
            if (sql.contains("ORDER")) {
                sql = sql.substring(0, sql.indexOf("ORDER"));
            }
            if (sql.contains("LIMIT")) {
                sql = sql.substring(0, sql.indexOf("LIMIT"));
            }
            sql = "SELECT  count(1) as totalCount " + sql;
            logger.info("getTotalRecords after:sql=" + sql);
        }
        try {
            ResultSet rs = readDBEngine.executeQuery(sql);
            if (rs.next()) {
                count = rs.getInt(getColIdxByLabelName("totalCount", rs));
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 获分页
     * 
     * @param pageSize
     *            分页大小
     * @param pageNo
     *            页码
     * @return 返回分页对象
     */
    public DataPage<T> findPage(int pageSize, int pageNo) {
        String sql = "SELECT * FROM " + tableName;
        return findPage(sql, pageSize, pageNo);
    }

    /**
     * 根据别名获取所处的位置(由于目前不支持别名的查询，所以写了这个方法)
     * 
     * @return
     */
    public int getColIdxByLabelName(String labelName, ResultSet rs) throws SQLException {

        RowSetMetaDataImpl rowSetMD = (RowSetMetaDataImpl) rs.getMetaData();
        int i = rowSetMD.getColumnCount();
        for (int j = 1; j <= i; j++) {
            String str = rowSetMD.getColumnLabel(j);// 使用别名
            if ((str != null) && (labelName.equalsIgnoreCase(str)))
                return j;
        }
        throw new SQLException("列名无效：" + labelName);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableKeyId() {
        return tableKeyId;
    }

    public void setTableKeyId(String tableKeyId) {
        this.tableKeyId = tableKeyId;
    }

}
