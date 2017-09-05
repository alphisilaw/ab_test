package com.zhenai.channel_dispatcher.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.zhenai.channel_dispatcher.moudel.ChannelVersion;

@Repository
public class ChannelVersionDao extends BaseDao<ChannelVersion> {

	@Override
	protected String getKey() {
		return "versionId";
	}

	public List<ChannelVersion> findLstByChannelId(Integer channelId, Integer subChannelId) {
		
		List<Object> params = new ArrayList<Object>();
		
		String sql = "select cv.* from ChannelTest ct join ChannelVersion cv on ct.testId=cv.testId where cv.status=1 and ct.channelId=? ";
		params.add(channelId);
		if (subChannelId != null) {
			sql += " and ct.subChannelId=?";
			params.add(subChannelId);
		} else {
			sql += " and ct.subChannelId is null";
		}
		
		return mainNamedTemplate.getJdbcOperations().query(sql, rowMapper, params.toArray(new Object[params.size()]));
	}

	public ChannelVersion findVersionById(Integer versionId) {
		String sql = "select * from ChannelVersion where status=1 and versionId=?";
		ChannelVersion version = null;
		try {
			version = mainNamedTemplate.getJdbcOperations().queryForObject(sql, rowMapper, versionId);
		} catch (Exception e) {
			
		}
		return version;
	}
}
