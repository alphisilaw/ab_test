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
import com.web.abt.m.model.UserProjectCaseGoalModel;

/**
 */
public class UserProjectCaseGoalDao extends BaseDao<UserProjectCaseGoalModel> {

    /**
     * 日志处理类实例
     */
    private static Log logger = LogFactory.getLog(UserProjectCaseGoalDao.class);

    /**
     * banner_info表
     */
    private static final String TABLE = "user_project_case_goal";

    /**
     * 主表ID
     */
    protected static final String TABLE_KEY_ID = "goalId";

    /**
     * AlbumDao对象
     */
    private static UserProjectCaseGoalDao instance = new UserProjectCaseGoalDao(TABLE, TABLE_KEY_ID);

    /**
     * 默认构造方法
     */
    private UserProjectCaseGoalDao(String tableName, String tableKeyId) {
        super(tableName, tableKeyId);
    }

    /**
     * 获得对象实例
     */
    public static UserProjectCaseGoalDao getInstance() {
        return instance;
    }

    /**
     * 插入一条数据
     * 
     * @param model
     * @return 返回插入影响行数
     */
    public UserProjectCaseGoalModel insert(UserProjectCaseGoalModel model) {
        DBEngine localEngine = DBFactory.getKeepConnectionDBEngine(LOCAL_WRITE_POOL);
        String sql = "INSERT INTO "
                     + TABLE
                     + "(goalName,goalTypeId,goalSelector,seq,caseId,projectId,goalBuizType,createTime,updateTime,isMaster) VALUES (?,?,?,?,?,?,?,?,?,?)";
        Object[] paramObj = {model.getGoalName(),
        		model.getGoalTypeId(),
        		model.getGoalSelector(),
        		model.getSeq(),
        		model.getCaseId(),
        		model.getProjectId(),
        		model.getGoalBuizType(),
        		model.getCreateTime(),
        		model.getUpdateTime(),
        		model.getIsMaster()};
        int id = 0;
        try {
            id = localEngine.executeUpdate(sql, paramObj);
            ResultSet rs = localEngine.executeQuery("select last_insert_id() as id");
            if (rs.next()) {
                id = rs.getInt("id");
                // 加密ID
                model.setGoalId(id);
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
    public boolean update(UserProjectCaseGoalModel model) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET goalName=?,goalTypeId=?,goalSelector=?,seq=?,caseId=?,projectId=?,goalBuizType=?,createTime=?,updateTime=?,isMaster=? WHERE goalId = ? ";
        Object[] paramObj = {model.getGoalName(),
        		model.getGoalTypeId(),
        		model.getGoalSelector(),
        		model.getSeq(),
        		model.getCaseId(),
        		model.getProjectId(),
        		model.getGoalBuizType(),
        		model.getCreateTime(),
        		model.getUpdateTime(),
        		model.getIsMaster(),
                model.getGoalId()};
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

    public DataPage<UserProjectCaseGoalModel> getUserProjectCaseGoalModelPage(UserProjectCaseGoalModel model,
                                                             int pageNo,
                                                             int pageSize) {
       
        String sql = "SELECT * FROM " + TABLE + " WHERE 1=1";
        sql = sql + " ORDER BY goalId desc";
        logger.info("sql=" + sql);
        return super.findPage(sql, pageSize, pageNo);
    }
    
    public DataPage<UserProjectCaseGoalModel> doSearchPage(
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
    
    public List<UserProjectCaseGoalModel> doSearchListSQL(
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

    public boolean updateGoal(int goalId, String gName, int gType, 
    		String gSelector, int gBuizType, int isMaster) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET goalName=?,goalTypeId=?,goalSelector=?,goalBuizType=?,isMaster=? WHERE goalId = ? ";
        Object[] paramObj = {gName, gType, gSelector, gBuizType, isMaster,goalId};
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
    
    public List<UserProjectCaseGoalModel> doSearchListByCaseId(int caseId, int projectId) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(TABLE).append(" WHERE caseId=? AND projectId=?");
		Object[] paramObj = {caseId, projectId};
		
		logger.info("sql=" + sql.toString());
		return this.queryModelList(sql.toString(), paramObj);
	}
    
    public List<UserProjectCaseGoalModel> doSearchListByCaseIdAndBuizType(int caseId, 
    		int projectId, int goalBuizType) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(TABLE).append(" WHERE caseId=? AND projectId=? AND goalBuizType = ?");
		Object[] paramObj = {caseId, projectId, goalBuizType};
		
		logger.info("sql=" + sql.toString());
		return this.queryModelList(sql.toString(), paramObj);
	}
    
    public boolean deleteByProjectId(Integer pid) {
        String sql = "DELETE FROM " + TABLE + " WHERE projectId = ?";
        Object[] paramObjs = {pid};
        try {
            return writeDBEngine.executeUpdate(sql, paramObjs) > 0;
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean isMasterGoalNotExistByCaseId(int caseId) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(1) AS NUM FROM ").append(TABLE).append(" WHERE caseId=? AND isMaster=1");
		Object[] paramObj = {caseId};
		logger.info("sql=" + sql.toString());
		try {
            ResultSet rs = readDBEngine.executeQuery(sql.toString(), paramObj);
            if (rs.next()) {
                return rs.getInt("NUM") <= 0;
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return false;
	}
    
    public boolean updateMasterGoalStatus(int goalId, int masterStatus) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET isMaster=? WHERE goalId = ? ";
        Object[] paramObj = {masterStatus, goalId};
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
    
    public boolean updateMasterGoalStatusByCaseId(int caseId, int masterStatus) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET isMaster=? WHERE caseId = ? ";
        Object[] paramObj = {masterStatus, caseId};
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
    
    public boolean isGoalNotExistByCaseId(int gTypeId, int gBuizType, int caseId) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(1) AS NUM FROM ").append(TABLE).append(" WHERE caseId=? AND goalTypeId=? AND goalBuizType = ?");
		Object[] paramObj = {caseId, gTypeId, gBuizType};
		logger.info("sql=" + sql.toString());
		try {
            ResultSet rs = readDBEngine.executeQuery(sql.toString(), paramObj);
            if (rs.next()) {
                return rs.getInt("NUM") <= 0;
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return false;
	}
    
    public boolean isGoalNameNotExistByCaseId(String gName, int gBuizType, int caseId) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(1) AS NUM FROM ").append(TABLE).append(" WHERE caseId=? AND goalName=? AND goalBuizType = ?");
		Object[] paramObj = {caseId, gName, gBuizType};
		logger.info("sql=" + sql.toString());
		try {
            ResultSet rs = readDBEngine.executeQuery(sql.toString(), paramObj);
            if (rs.next()) {
                return rs.getInt("NUM") <= 0;
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return false;
	}
}
