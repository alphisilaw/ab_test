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
import com.web.abt.m.model.UserInfoModel;

/**
 */
public class UserInfoDao extends BaseDao<UserInfoModel> {

    /**
     * 日志处理类实例
     */
    private static Log logger = LogFactory.getLog(UserInfoDao.class);

    /**
     * banner_info表
     */
    private static final String TABLE = "user_info";

    /**
     * 主表ID
     */
    protected static final String TABLE_KEY_ID = "uid";

    /**
     * AlbumDao对象
     */
    private static UserInfoDao instance = new UserInfoDao(TABLE, TABLE_KEY_ID);

    /**
     * 默认构造方法
     */
    private UserInfoDao(String tableName, String tableKeyId) {
        super(tableName, tableKeyId);
    }

    /**
     * 获得对象实例
     */
    public static UserInfoDao getInstance() {
        return instance;
    }

    /**
     * 插入一条数据
     * 
     * @param model
     * @return 返回插入影响行数
     */
    public UserInfoModel insert(UserInfoModel model) {
        DBEngine localEngine = DBFactory.getKeepConnectionDBEngine(LOCAL_WRITE_POOL);
        String sql = "INSERT INTO "
                     + TABLE
                     + "(nickName,password,sex,mobile,pickey,email,birthday,country,province,city,createTime,iphoneToken,baiduChannelId,baiduUserId,platform,device,softversion,channel,phonecode,locale,countrycode,accessToken,online,qqOpenId,status,systemVersion,privateFlag,wallpaper,sinaOpenId,xiaomiUserId,jpushUserId,updateAt) VALUES (?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] paramObj = {model.getNickName(),
        		model.getPassword(),
        		model.getSex(),
        		model.getMobile(),
        		model.getPickey(),
        		model.getEmail(),
        		model.getBirthday(),
        		model.getCountry(),
        		model.getProvince(),
        		model.getCity(),
        		model.getIphoneToken(),
        		model.getBaiduChannelId(),
        		model.getBaiduUserId(),
        		model.getPlatform(),
        		model.getDevice(),
        		model.getSoftversion(),
        		model.getChannel(),
        		model.getPhonecode(),
        		model.getLocale(),
        		model.getCountrycode(),
        		model.getAccessToken(),
        		model.getOnline(),
        		model.getQqOpenId(),
        		model.getStatus(),
        		model.getSystemVersion(),
        		model.getPrivateFlag(),
        		model.getWallpaper(),
        		model.getSinaOpenId(),
        		model.getXiaomiUserId(),
        		model.getJpushUserId(),
        		model.getUpdateAt()};
        int id = 0;
        try {
            id = localEngine.executeUpdate(sql, paramObj);
            ResultSet rs = localEngine.executeQuery("select last_insert_id() as id");
            if (rs.next()) {
                id = rs.getInt("id");
                // 加密ID
                model.setUid(id);
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
    public boolean update(UserInfoModel model) {
        String sql = "UPDATE  "
                     + TABLE
                     + " SET nickName=?,password=?,sex=?,mobile=?,pickey=?,email=?,birthday=?,country=?,province=?,city=?,createTime=?,iphoneToken=?,baiduChannelId=?,baiduUserId=?,platform=?,device=?,softversion=?,channel=?,phonecode=?,locale=?,countrycode=?,accessToken=?,online=?,qqOpenId=?,status=?,systemVersion=?,privateFlag=?,wallpaper=?,sinaOpenId=?,xiaomiUserId=?,jpushUserId=?,updateAt=? WHERE uid = ? ";
        Object[] paramObj = {model.getNickName(),
        		model.getPassword(),
        		model.getSex(),
        		model.getMobile(),
        		model.getPickey(),
        		model.getEmail(),
        		model.getBirthday(),
        		model.getCountry(),
        		model.getProvince(),
        		model.getCity(),
        		model.getCreateTime(),
        		model.getIphoneToken(),
        		model.getBaiduChannelId(),
        		model.getBaiduUserId(),
        		model.getPlatform(),
        		model.getDevice(),
        		model.getSoftversion(),
        		model.getChannel(),
        		model.getPhonecode(),
        		model.getLocale(),
        		model.getCountrycode(),
        		model.getAccessToken(),
        		model.getOnline(),
        		model.getQqOpenId(),
        		model.getStatus(),
        		model.getSystemVersion(),
        		model.getPrivateFlag(),
        		model.getWallpaper(),
        		model.getSinaOpenId(),
        		model.getXiaomiUserId(),
        		model.getJpushUserId(),
        		model.getUpdateAt(),
                model.getUid()};
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

    public DataPage<UserInfoModel> getUserInfoModelPage(UserInfoModel model,
                                                             int pageNo,
                                                             int pageSize) {
        
        String sql = "SELECT * FROM " + TABLE + " WHERE 1=1";
        sql = sql + " ORDER BY uid desc";
        logger.info("sql=" + sql);
        return super.findPage(sql, pageSize, pageNo);
    }
    
    public DataPage<UserInfoModel> doSearchPage(
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
    
    public List<UserInfoModel> doSearchListSQL(
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

    public UserInfoModel findByEmailByBackRead(String email) {
        String sql = "SELECT * FROM " + TABLE + " WHERE email = ?";
        Object[] paramObjs = {email};
        return queryModelByBackRead(sql, paramObjs);
    }
}
