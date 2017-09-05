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
import com.web.abt.m.model.UserProjectModel;

/**
 */
public class UserProjectDao extends BaseDao<UserProjectModel> {

    /**
     * 日志处理类实例
     */
    private static Log logger = LogFactory.getLog(UserProjectDao.class);

    /**
     * banner_info表
     */
    private static final String TABLE = "user_project";
    
    private static final String CASE_TABLE = "user_project_case";

    /**
     * 主表ID
     */
    protected static final String TABLE_KEY_ID = "projectId";

    /**
     * AlbumDao对象
     */
    private static UserProjectDao instance = new UserProjectDao(TABLE, TABLE_KEY_ID);

    /**
     * 默认构造方法
     */
    private UserProjectDao(String tableName, String tableKeyId) {
        super(tableName, tableKeyId);
    }

    /**
     * 获得对象实例
     */
    public static UserProjectDao getInstance() {
        return instance;
    }

    /**
     * 插入一条数据
     * 
     * @param model
     * @return 返回插入影响行数
     */
    public UserProjectModel insert(UserProjectModel model) {
        DBEngine localEngine = DBFactory.getKeepConnectionDBEngine(LOCAL_WRITE_POOL);
        String sql = "INSERT INTO "
                     + TABLE
                     + "(projectName,description,platform,ownerId,createTime,updateTime) VALUES (?,?,?,?,?,?)";
        Object[] paramObj = {model.getProjectName(),
        		model.getDescription(),
        		model.getPlatform(),
        		model.getOwnerId(),
        		model.getCreateTime(),
        		model.getUpdateTime()};
        int id = 0;
        try {
            id = localEngine.executeUpdate(sql, paramObj);
            ResultSet rs = localEngine.executeQuery("select last_insert_id() as id");
            if (rs.next()) {
                id = rs.getInt("id");
                // 加密ID
                model.setProjectId(id);
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
    public boolean isEmpty(int projectId){
    	String sql = "SELECT COUNT(caseId) FROM "
    			+ CASE_TABLE 
    			+ " WHERE caseStatus>-2 AND projectId="+projectId;
    	
    	return super.getTotalRecords(sql)==0;
    }

    /**
     * 修改一条数据
     * 
     * @param model
     * @return 修改成功返回true,否则返回false
     */
    public boolean update(UserProjectModel model) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET projectName=?,description=?,platform=?,ownerId=?,createTime=? WHERE projectId = ? ";
        Object[] paramObj = {model.getProjectName(),
        		model.getDescription(),
        		model.getPlatform(),
        		model.getOwnerId(),
        		model.getCreateTime()};
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

    public DataPage<UserProjectModel> getUserProjectModelPage(UserProjectModel model,
                                                             int pageNo,
                                                             int pageSize) {
        
        String sql = "SELECT * FROM " + TABLE + " WHERE 1=1";
        sql = sql + " ORDER BY projectId desc";
        logger.info("sql=" + sql);
        return super.findPage(sql, pageSize, pageNo);
    }
    
    public DataPage<UserProjectModel> doSearchPage(
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
    
    public List<UserProjectModel> doSearchListSQL(
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

	public DataPage<UserProjectModel> getUserProjectPage(int pageSize, int pageNum) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(TABLE)
			.append(" ORDER BY projectId DESC");
		logger.info("sql=" + sql.toString());
		return super.findPage(sql.toString(), pageSize, pageNum);
	}
	
	public boolean updateProjectByProjectID(int projectId, String pName, String pDesc) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET projectName=?,description=? WHERE projectId = ? ";
        Object[] paramObj = {pName, pDesc, projectId};
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
}
