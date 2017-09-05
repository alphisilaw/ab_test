package com.zhenai.channel_manager.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.zhenai.channel_manager.moudel.ChannelTest;


@Repository
public class ChannelTestDao extends BaseDao<ChannelTest> {
	
	@Override
	protected String getKey() {
		return "testId";
	}

	public ChannelTest findByChannelId(Integer channelId, Integer subChannelId) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from ChannelTest where channelId = ? ";
		params.add(channelId);
		if (subChannelId != null) {
			sql += " and subChannelId= ? ";
			params.add(subChannelId);
		} else {
			sql += " and subChannelId is null ";
		}
		ChannelTest channelTest = null;
		try {
			channelTest = mainNamedTemplate.getJdbcOperations()
					.queryForObject(sql, rowMapper, params.toArray(new Object[params.size()]));
		} catch (Exception e) {
			
		}
		return channelTest;
	}
}
