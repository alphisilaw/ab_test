package com.web.abt.m.dao;

import java.sql.ResultSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.coola.jutil.data.DataPage;
import com.coola.jutil.sql.DBEngine;
import com.coola.jutil.sql.DBFactory;
import com.web.abt.m.common.SQLCondition;
import com.web.abt.m.common.SQLCondition.ConditionTypes;
import com.web.abt.m.model.UserProjectCaseGoalTypeModel;

/**
 */
public class UserProjectCaseGoalTypeDao extends BaseDao<UserProjectCaseGoalTypeModel> {

    /**
     * 日志处理类实例
     */
    private static Log logger = LogFactory.getLog(UserProjectCaseGoalTypeDao.class);

    /**
     * banner_info表
     */
    private static final String TABLE = "user_project_case_goal_type";

    /**
     * 主表ID
     */
    protected static final String TABLE_KEY_ID = "id";

    /**
     * AlbumDao对象
     */
    private static UserProjectCaseGoalTypeDao instance = new UserProjectCaseGoalTypeDao(TABLE, TABLE_KEY_ID);

    /**
     * 默认构造方法
     */
    private UserProjectCaseGoalTypeDao(String tableName, String tableKeyId) {
        super(tableName, tableKeyId);
    }

    /**
     * 获得对象实例
     */
    public static UserProjectCaseGoalTypeDao getInstance() {
        return instance;
    }

    /**
     * 插入一条数据
     * 
     * @param model
     * @return 返回插入影响行数
     */
    public UserProjectCaseGoalTypeModel insert(UserProjectCaseGoalTypeModel model) {
        DBEngine localEngine = DBFactory.getKeepConnectionDBEngine(LOCAL_WRITE_POOL);
        String sql = "INSERT INTO "
                     + TABLE
                     + "(goalTypeCode,goalTypeName,createTime,updateTime) VALUES (?,?,?,?)";
        Object[] paramObj = {model.getGoalTypeCode(),
        		model.getGoalTypeName(),
        		model.getCreateTime(),
        		model.getUpdateTime()};
        int id = 0;
        try {
            id = localEngine.executeUpdate(sql, paramObj);
            ResultSet rs = localEngine.executeQuery("select last_insert_id() as id");
            if (rs.next()) {
                id = rs.getInt("id");
                // 加密ID
                model.setId(id);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        finally {
            localEngine.close();
        }
        return model;
    }

    /**
     * 修改一条数据
     * 
     * @param model
     * @return 修改成功返回true,否则返回false
     */
    public boolean update(UserProjectCaseGoalTypeModel model) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET goalTypeCode=?,goalTypeName=?,createTime=? WHERE goalId = ? ";
        Object[] paramObj = {model.getGoalTypeCode(),
        		model.getGoalTypeName(),
        		model.getCreateTime(),
                model.getId()};
        try {
            logger.info("sql:" + sql);
            return writeDBEngine.executeUpdate(sql, paramObj) > 0;
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return false;
    }

    public DataPage<UserProjectCaseGoalTypeModel> getUserProjectCaseGoalTypeModelPage(UserProjectCaseGoalTypeModel model,
                                                             int pageNo,
                                                             int pageSize) {
        
        String sql = "SELECT * FROM " + TABLE + " WHERE 1=1";
        sql = sql + " ORDER BY id desc";
        logger.info("sql=" + sql);
        return super.findPage(sql, pageSize, pageNo);
    }
    
    public DataPage<UserProjectCaseGoalTypeModel> doSearchPage(
			List<SQLCondition> conditions, String orderBy, int limitNum,
			int rows, int page) {
		
		StringBuffer sqlSel = new StringBuffer();
		StringBuffer sqlCond = new StringBuffer();
		sqlSel.append("SELECT * FROM ").append(TABLE);

		boolean isFirst = true;
		for (SQLCondition con : conditions) {
			if (isFirst) {
				sqlSel.append(" WHERE ");
				isFirst = false;
			} else {
				sqlCond.append(" AND");
			}
			;
			sqlCond.append(" ").append(con.getConName()).append(" ")
					.append(con.getOperation()).append(" ");
			
			if(con.getConType().equals(ConditionTypes.STRING)){
				sqlCond.append("'").append(con.getConValue()).append("'");
			}else{
				sqlCond.append(con.getConValue());
			}
		}
		if (StringUtils.isNotBlank(orderBy))
			sqlCond.append(" ORDER BY ").append(orderBy);
		if (limitNum > 0)
			sqlCond.append(" LIMIT ").append(limitNum);
		String finalSql = sqlSel.append(sqlCond).toString();
		logger.info("sql=" + finalSql);
		return this.findPage(finalSql, rows, page);
	}
    
    public List<UserProjectCaseGoalTypeModel> doSearchListSQL(
			List<SQLCondition> conditions, String orderBy, int limitNum) {
		
		StringBuffer sqlSel = new StringBuffer();
		StringBuffer sqlCond = new StringBuffer();
		sqlSel.append("SELECT * FROM ").append(TABLE);

		boolean isFirst = true;
		for (SQLCondition con : conditions) {
			if (isFirst) {
				sqlSel.append(" WHERE ");
				isFirst = false;
			} else {
				sqlCond.append(" AND");
			}
			;
			sqlCond.append(" ").append(con.getConName()).append(" ")
					.append(con.getOperation()).append(" ");
			
			if(con.getConType().equals(ConditionTypes.STRING)){
				sqlCond.append("'").append(con.getConValue()).append("'");
			}else{
				sqlCond.append(con.getConValue());
			}
		}
		if (StringUtils.isNotBlank(orderBy))
			sqlCond.append(" ORDER BY ").append(orderBy);
		if (limitNum > 0)
			sqlCond.append(" LIMIT ").append(limitNum);
		String finalSql = sqlSel.append(sqlCond).toString();
		logger.info("sql=" + finalSql);
		return this.queryModelList(finalSql);
	}

    public List<UserProjectCaseGoalTypeModel> findAllByBuizType(Object... goalBuizType) {
        String sql = null;
        if (goalBuizType.length == 1) {
        	sql = "SELECT * FROM " + TABLE + " WHERE goalBuizType = ?";
        } else if (goalBuizType.length == 2) {
        	sql = "SELECT * FROM " + TABLE + " WHERE goalBuizType IN (?,?)";
        }
        return queryModelList(sql, goalBuizType);
    }
}
