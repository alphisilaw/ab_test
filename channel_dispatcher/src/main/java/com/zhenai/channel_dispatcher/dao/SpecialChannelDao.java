package com.zhenai.channel_dispatcher.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SpecialChannelDao {

	@Resource
	private JdbcTemplate mainJdbc;
	
	public boolean existsInSpecialList(Integer channelId) {
		String sql = "select 1 from SpecialChannel where channelId=?";
		List<Map<String, Object>> list = mainJdbc.queryForList(sql, channelId);
		if (list == null || list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
}
