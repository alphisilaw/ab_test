package com.zhenai.channel_manager.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.zhenai.channel_manager.moudel.KPIData;
import com.zhenai.channel_manager.moudel.PvUvData;
import com.zhenai.channel_manager.util.CommonUtil;

@Repository
public class KPIDataDao {
	
	@Resource
	private JdbcTemplate dataJdbc;

	public Integer getUvByVersionId(Integer versionId) {
		String sql = new StringBuilder("select sum(uv) uv from zhenai.r_abtesting_pv_uv_h ")
							.append("where testversionid=? ")
							.append("and clickid='0'")
							.toString();
		Integer uv = 0;
		try {
			uv = dataJdbc.queryForObject(sql, Integer.class, versionId);
		} catch (Exception e) {
			
		}
		return uv == null ? 0 : uv;
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
		return dataJdbc.query(sql.toString(), 
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
		return dataJdbc.query(sql.toString(), 
				  			  BeanPropertyRowMapper.newInstance(PvUvData.class), 
				  			  params.toArray(new Object[params.size()]));
	}
}
