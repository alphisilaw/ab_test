package com.web.abt.m.dao;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.coola.jutil.data.DataPage;
import com.coola.jutil.sql.DBEngine;
import com.coola.jutil.sql.DBFactory;
import com.web.abt.m.common.SQLCondition;
import com.web.abt.m.common.SQLCondition.ConditionTypes;
import com.web.abt.m.model.UserProjectCaseModel;
import com.web.abt.m.model.UserProjectModel;

/**
 */
public class UserProjectCaseDao extends BaseDao<UserProjectCaseModel> {

    /**
     * 日志处理类实例
     */
    private static Log logger = LogFactory.getLog(UserProjectCaseDao.class);

    /**
     * banner_info表
     */
    private static final String TABLE = "user_project_case";
    
    private static final String PROJECT_TABLE= "user_project";

    /**
     * 主表ID
     */
    protected static final String TABLE_KEY_ID = "caseId";

    /**
     * AlbumDao对象
     */
    private static UserProjectCaseDao instance = new UserProjectCaseDao(TABLE, TABLE_KEY_ID);

    /**
     * 默认构造方法
     */
    private UserProjectCaseDao(String tableName, String tableKeyId) {
        super(tableName, tableKeyId);
    }

    /**
     * 获得对象实例
     */
    public static UserProjectCaseDao getInstance() {
        return instance;
    }

    /**
     * 插入一条数据
     * 
     * @param model
     * @return 返回插入影响行数
     */
    public UserProjectCaseModel insert(UserProjectCaseModel model) {
        DBEngine localEngine = DBFactory.getKeepConnectionDBEngine(LOCAL_WRITE_POOL);
        String sql = "INSERT INTO "
                     + TABLE
                     + "(caseName,buizType,projectId,caseStatus,dayCount,verCount,curGoalCount,maxGoalCount,url,createTime,updateTime,copySN,copyPrototypeId,isMobile) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] paramObj = {model.getCaseName(),
        		model.getBuizType(),
        		model.getProjectId(),
        		model.getCaseStatus(),
        		model.getDayCount(),
        		model.getVerCount(),
        		model.getCurGoalCount(),
        		model.getMaxGoalCount(),
        		model.getUrl(),
        		model.getCreateTime(),
        		model.getUpdateTime(),
        		model.getCopySN(),
        		model.getCopyPrototypeId(),
        		model.getIsMobile()};
        int id = 0;
        try {
            id = localEngine.executeUpdate(sql, paramObj);
            ResultSet rs = localEngine.executeQuery("select last_insert_id() as id");
            if (rs.next()) {
                id = rs.getInt("id");
                // 加密ID
                model.setCaseId(id);
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
    public boolean update(UserProjectCaseModel model) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET caseName=?,buizType=?,projectId=?,caseStatus=?,dayCount=?,verCount=?,curGoalCount=?,maxGoalCount=?,url=?,createTime=? WHERE caseId = ? ";
        Object[] paramObj = {model.getCaseName(),
        		model.getBuizType(),
        		model.getProjectId(),
        		model.getCaseStatus(),
        		model.getDayCount(),
        		model.getVerCount(),
        		model.getCurGoalCount(),
        		model.getMaxGoalCount(),
        		model.getUrl(),
        		model.getCreateTime(),
                model.getCaseId()};
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

    public DataPage<UserProjectCaseModel> getUserProjectCaseModelPage(UserProjectCaseModel model,
                                                             int pageNo,
                                                             int pageSize) {
        String sql = "SELECT * FROM " + TABLE + " WHERE 1=1";
        sql = sql + " ORDER BY caseId desc";
        logger.info("sql=" + sql);
        return super.findPage(sql, pageSize, pageNo);
    }
    
    public DataPage<UserProjectCaseModel> doSearchPage(
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
    
    public List<UserProjectCaseModel> doSearchListSQL(
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

    public DataPage<UserProjectCaseModel> getAvailCasePageByProjectId(
			int projectId, int pageSize, int pageNum, String expriType, String orderBy, String url) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(TABLE)
			.append(" WHERE projectId = ").append(projectId);
		if(expriType.trim().equalsIgnoreCase("valid")){
			sql.append(" AND caseStatus >= 0 ");
		}else if (expriType.trim().equalsIgnoreCase("archive")) {
			sql.append(" AND caseStatus = -1 ");
		}
		if (StringUtils.isNotEmpty(url)) {
			sql.append(" AND url like '%").append(url).append("%' ");
		}
		if( orderBy.trim().equalsIgnoreCase("status")){
			sql.append(" ORDER BY caseStatus DESC");
		}else if (orderBy.trim().equalsIgnoreCase("crtime")) {
			sql.append(" ORDER BY createTime DESC");//createTime
		}
		logger.info("sql=" + sql.toString());
		return super.findPage(sql.toString(), pageSize, pageNum);
	}
    
    public DataPage<UserProjectCaseModel> getDeleteCasePageByProjectId(
			int projectId, int pageSize, int pageNum) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(TABLE)
			.append(" WHERE projectId = ").append(projectId)
			.append(" AND caseStatus = -1 ORDER BY caseId DESC");
		logger.info("sql=" + sql.toString());
		return super.findPage(sql.toString(), pageSize, pageNum);
	}
    
    public boolean runCase(int caseId, Date firstRunDate) {
        String sql = "";
        if(firstRunDate == null){
        	sql = "UPDATE  "
                    + TABLE
                    + " SET caseStatus=2, startRunTime = ? WHERE caseId = ? ";
        	Object[] paramObj = {new Date(), caseId};
            try {
                logger.info("sql:" + sql);
                return writeDBEngine.executeUpdate(sql, paramObj) > 0;
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }else{
        	sql = "UPDATE  "
                    + TABLE
                    + " SET caseStatus=2 WHERE caseId = ? ";
        	Object[] paramObj = {caseId};
            try {
                logger.info("sql:" + sql);
                return writeDBEngine.executeUpdate(sql, paramObj) > 0;
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }
        return false;
    }
    
    public boolean updateCaseStatus(int caseId, int statusId) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET caseStatus=? WHERE caseId = ? ";
        Object[] paramObj = {statusId, caseId};
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
    
    public boolean updateCaseName(int caseId, String caseName) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET caseName=? WHERE caseId = ? ";
        Object[] paramObj = {caseName, caseId};
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
    
    public boolean updateDayCount(int caseId) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET dayCount=dayCount+1 WHERE caseId = ? ";
        Object[] paramObj = {caseId};
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
    
    public boolean addVersionCount(int caseId) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET verCount=verCount+1 WHERE caseId = ? ";
        Object[] paramObj = {caseId};
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
    
    public boolean minusVersionCount(int caseId) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET verCount=verCount-1 WHERE caseId = ? ";
        Object[] paramObj = {caseId};
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
    
    public boolean addCurGoalCount(int caseId) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET curGoalCount=curGoalCount+1 WHERE caseId = ? ";
        Object[] paramObj = {caseId};
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
    
    public boolean minusCurGoalCount(int caseId) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET curGoalCount=curGoalCount-1 WHERE caseId = ? ";
        Object[] paramObj = {caseId};
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
    
    public List<UserProjectCaseModel> doSearchRunningListByProjectId(int projectId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(TABLE).append(" WHERE projectId=? AND caseStatus=?");
		Object[] paramObj = {projectId, 2};
		
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
    
    public boolean isSameCaseNameExisted(int projectId, String caseName) {
    	//新增caseStatus>=-1的条件，因为caseStatus=-2意味着实验已经被彻底删除
        String sql = "SELECT count(1) as num FROM " + TABLE + " WHERE caseStatus>=-1 AND projectId = ? AND caseName = ?";
        Object[] paramObjs = {projectId, caseName};
        try {
            ResultSet rs = readDBEngine.executeQuery(sql, paramObjs);
            if (rs.next()) {
                int num = rs.getInt("num");
                if(num > 0)
                	return true;
                else
                	return false;
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return false;
    }
    
    public UserProjectModel getProjectByCaseId(int caseId){
    	String sql =  "SELECT "+PROJECT_TABLE+".* FROM " + PROJECT_TABLE 
    			   + " JOIN "+TABLE
    			   + " ON "+TABLE+".projectId="+PROJECT_TABLE+".projectId"
    			   + " WHERE caseId = ? ";
    	UserProjectModel model = new UserProjectModel();
        Object[] paramObjs = {caseId};
        try {
            ResultSet rs = readDBEngine.executeQuery(sql, paramObjs);
            if (rs.next()) {
                return model.getModelByRs(rs);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    	return null;
    }
    
    public int getCopySN( int oldCaseId ){
    	String sql = "SELECT MAX(copySN)+1 as newCopySN FROM " + TABLE + " WHERE copyPrototypeId = ?";
    	Object[] paramObjs = {oldCaseId};
    	try {
            ResultSet rs = readDBEngine.executeQuery(sql, paramObjs);
            int newCopySN;
            if (rs.next()) {
                newCopySN = rs.getInt("newCopySN");
            }else{
            	newCopySN=1;
            }
            return newCopySN;
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    	return 0;
    }

	public boolean isSameCaseUrlRunning(String cUrl) {
    	//新增caseStatus>0的条件，暂停或者运行中
        String sql = "SELECT count(1) as num FROM " + TABLE + " WHERE caseStatus = 2 AND url = ?";
        try {
            ResultSet rs = readDBEngine.executeQuery(sql, cUrl);
            if (rs.next()) {
                int num = rs.getInt("num");
                if(num > 0)
                	return true;
                else
                	return false;
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return false;
	}
}
