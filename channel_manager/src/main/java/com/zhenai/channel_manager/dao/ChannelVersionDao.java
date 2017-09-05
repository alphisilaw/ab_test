package com.zhenai.channel_manager.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zhenai.channel_manager.moudel.ChannelVersion;

@Repository
public class ChannelVersionDao extends BaseDao<ChannelVersion> {

	@Override
	protected String getKey() {
		return "versionId";
	}

	public List<ChannelVersion> findLstByChannelId(Integer channelId, Integer subChannelId) {
		
		List<Object> params = new ArrayList<Object>();
		
		String sql = "select cv.* from ChannelTest ct join ChannelVersion cv on ct.testId=cv.testId where ct.channelId=? and cv.status=1";
		params.add(channelId);
		if (subChannelId != null) {
			sql += " and ct.subChannelId=?";
			params.add(subChannelId);
		} else {
			sql += " and ct.subChannelId is null";
		}
		
		return mainNamedTemplate.getJdbcOperations().query(sql, rowMapper, params.toArray(new Object[params.size()]));
	}
	
	public List<ChannelVersion> findLstForData(Integer channelId, Integer subChannelId) {

		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder()
		.append("select cv.* from ChannelTest ct join ChannelVersion cv on ct.testId=cv.testId where ct.channelId=? ");
		params.add(channelId);
		if (subChannelId != null) {
			sql.append(" and ct.subChannelId=?");
			params.add(subChannelId);
		} else {
			sql.append(" and ct.subChannelId is null");
		}
		sql.append(" order by cv.createtime ");
		
		return mainNamedTemplate.getJdbcOperations()
				.query(sql.toString(), rowMapper, params.toArray(new Object[params.size()]));
	}

	public List<Map<String, Object>> findLstByTestId(List<Integer> testIds) {
		
		if (testIds == null || testIds.size() == 0) {
			return new ArrayList<Map<String, Object>>();
		}
		List<Object> params = new ArrayList<Object>();
		StringBuilder sqlbuilder = new StringBuilder()
			.append("select cv.* from ChannelVersion cv where cv.status=1 and cv.testId in(");
		for (Integer integer : testIds) {
			sqlbuilder.append("?,");
			params.add(integer);
		}
		String sql = sqlbuilder.subSequence(0, sqlbuilder.length() - 1).toString() + ")";
		return mainNamedTemplate.getJdbcOperations().queryForList(sql, params.toArray(new Object[params.size()]));
	}

	public void setStatus(Integer testId, Integer status) {
		String sql = "update ChannelVersion set status=? where testId=?";
		mainNamedTemplate.getJdbcOperations().update(sql, status, testId);
	}
}
