package com.web.abt.m.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.web.abt.m.model.KPIData;
import com.web.abt.m.model.PvUvData;
import com.web.abt.m.util.CommonUtil;

public class KPIDataDao {
	/**
	 * jdbc.data.database=10.10.9.165/rdb
jdbc.data.user=postgres
jdbc.data.password=etl1\#
	 */
	
	private static KPIDataDao instance = new KPIDataDao();
	
	private JdbcTemplate viewTemplate;

	private KPIDataDao() {
		ResourceBundle prop = ResourceBundle.getBundle("jdbc");
		PGPoolingDataSource source = new PGPoolingDataSource();  
		source.setDataSourceName("abt_data_view");
		source.setServerName(StringUtils.trim(prop.getString("jdbc.data.server")));
		source.setPortNumber(Integer.valueOf(StringUtils.trim(prop.getString("jdbc.data.port"))));
		source.setUser(StringUtils.trim(prop.getString("jdbc.data.user")));
		source.setPassword(StringUtils.trim(prop.getString("jdbc.data.password")));  
		source.setDatabaseName(StringUtils.trim(prop.getString("jdbc.data.database")));
		source.setMaxConnections(10);
		viewTemplate = new JdbcTemplate(source);
	}

	/**
	 * 获得对象实例
	 */
	public static KPIDataDao getInstance() {
		return instance;
	}

	public List<KPIData> getKpiAllByTestId(Integer testId, Date begin, Date end) {
		List<Object> params = new ArrayList<Object>(3);
		StringBuilder sql = new StringBuilder("select * from zhenai.r_abtesting_kpiall_d where testid=? ");
		params.add(testId);
		if (begin != null) {
			int min = CommonUtil.evalInt(DateFormatUtils.format(begin, "yyyyMMdd"));
			sql.append(" and data_date >= ? ");
			params.add(min);
		}
		if (end != null) {
			int max = CommonUtil.evalInt(DateFormatUtils.format(end, "yyyyMMdd"));
			sql.append(" and data_date <= ? ");
			params.add(max);
		}
		return viewTemplate.query(sql.toString(), 
							  BeanPropertyRowMapper.newInstance(KPIData.class), 
							  params.toArray(new Object[params.size()]));
	}

	public List<PvUvData> getPvUvByTestId(Integer testId, Date begin, Date end) {
		List<Object> params = new ArrayList<Object>(3);
		StringBuilder sql = new StringBuilder("select * from zhenai.r_abtesting_pv_uv_d where testid=? ");
		params.add(testId);
		if (begin != null) {
			int min = CommonUtil.evalInt(DateFormatUtils.format(begin, "yyyyMMdd"));
			sql.append(" and data_date >= ? ");
			params.add(min);
		}
		if (end != null) {
			int max = CommonUtil.evalInt(DateFormatUtils.format(end, "yyyyMMdd"));
			sql.append(" and data_date <= ? ");
			params.add(max);
		}
		return viewTemplate.query(sql.toString(), 
				  			  BeanPropertyRowMapper.newInstance(PvUvData.class), 
				  			  params.toArray(new Object[params.size()]));
	}
}
