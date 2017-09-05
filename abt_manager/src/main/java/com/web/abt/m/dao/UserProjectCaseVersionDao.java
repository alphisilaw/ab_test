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
import com.web.abt.m.model.UserProjectCaseVersionModel;

/**
 */
public class UserProjectCaseVersionDao extends BaseDao<UserProjectCaseVersionModel> {

    /**
     * 日志处理类实例
     */
    private static Log logger = LogFactory.getLog(UserProjectCaseVersionDao.class);

    /**
     * banner_info表
     */
    private static final String TABLE = "user_project_case_version";

    /**
     * 主表ID
     */
    protected static final String TABLE_KEY_ID = "versionId";

    /**
     * AlbumDao对象
     */
    private static UserProjectCaseVersionDao instance = new UserProjectCaseVersionDao(TABLE, TABLE_KEY_ID);

    /**
     * 默认构造方法
     */
    private UserProjectCaseVersionDao(String tableName, String tableKeyId) {
        super(tableName, tableKeyId);
    }

    /**
     * 获得对象实例
     */
    public static UserProjectCaseVersionDao getInstance() {
        return instance;
    }

    /**
     * 插入一条数据
     * 
     * @param model
     * @return 返回插入影响行数
     */
    public UserProjectCaseVersionModel insert(UserProjectCaseVersionModel model) {
        DBEngine localEngine = DBFactory.getKeepConnectionDBEngine(LOCAL_WRITE_POOL);
        String sql = "INSERT INTO "
                     + TABLE
                     + "(versionName,versionType,versionStatus,caseId,projectId,jsCode,createTime,updateTime,percent,forwardUrl) VALUES (?,?,?,?,?,?,?,?,?,?)";
        Object[] paramObj = {model.getVersionName(),
        		model.getVersionType(),
        		model.getVersionStatus(),
        		model.getCaseId(),
        		model.getProjectId(),
        		model.getJsCode(),
        		model.getCreateTime(),
        		model.getUpdateTime(),
        		model.getPercent(),
        		model.getForwardUrl()};
        int id = 0;
        try {
            id = localEngine.executeUpdate(sql, paramObj);
            ResultSet rs = localEngine.executeQuery("select last_insert_id() as id");
            if (rs.next()) {
                id = rs.getInt("id");
                // 加密ID
                model.setVersionId(id);
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
    public boolean update(UserProjectCaseVersionModel model) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET versionName=?,versionType=?,versionStatus=?,caseId=?,projectId=?,jsCode=?,createTime=?,updateTime=?,percent=?,forwardUrl=? WHERE versionId = ? ";
        Object[] paramObj = {model.getVersionName(),
        		model.getVersionType(),
        		model.getVersionStatus(),
        		model.getCaseId(),
        		model.getProjectId(),
        		model.getJsCode(),
        		model.getCreateTime(),
        		model.getUpdateTime(),
        		model.getPercent(),
        		model.getForwardUrl(),
                model.getPercent()};
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

    public DataPage<UserProjectCaseVersionModel> getUserProjectCaseVersionModelPage(UserProjectCaseVersionModel model,
                                                             int pageNo,
                                                             int pageSize) {
        
        String sql = "SELECT * FROM " + TABLE + " WHERE 1=1";
        sql = sql + " ORDER BY versionId desc";
        logger.info("sql=" + sql);
        return super.findPage(sql, pageSize, pageNo);
    }
    
    public DataPage<UserProjectCaseVersionModel> doSearchPage(
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
    
    public List<UserProjectCaseVersionModel> doSearchListSQL(
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

    public boolean updateVersionJscode(int versionId, String jsCode, double percent,String versionName) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET jsCode=?,percent=?,updateTime=now(),versionName=? WHERE versionId = ? ";
        Object[] paramObj = {jsCode, percent, versionName,versionId};
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
    
    public boolean updateVersionStatus(int versionId, int statusId) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET versionStatus=? WHERE versionId = ? ";
        Object[] paramObj = {statusId, versionId};
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
    
    public List<UserProjectCaseVersionModel> doSearchListByCaseId(int caseId, int projectId) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(TABLE).append(" WHERE caseId=? AND projectId=?");
		Object[] paramObj = {caseId, projectId};
		
		logger.info("sql=" + sql.toString());
		return this.queryModelList(sql.toString(), paramObj);
	}
    
    public List<UserProjectCaseVersionModel> doSearchAvailableListByCaseId(int caseId, int projectId) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(TABLE).append(" WHERE caseId=? AND projectId=? AND versionStatus=?");
		Object[] paramObj = {caseId, projectId, 1};
		
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
    
    public boolean isVersionNameExist(int caseId,String versionName,int versionId){
    	String sql = "SELECT COUNT(1) AS num FROM "+TABLE+" WHERE caseId = ? AND versionName = ?";
    	if( versionId > 0 ){
    		sql = sql + " AND versionId != " + versionId;
    	}
    	Object[] paramObjs = {caseId,versionName};
    	try{
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
}
