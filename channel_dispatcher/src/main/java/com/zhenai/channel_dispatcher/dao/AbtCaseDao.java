package com.zhenai.channel_dispatcher.dao;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AbtCaseDao {
	
	private static final String WAP_URL = "http://m.zhenai.com/from.do";//?channelId=%s&subChannelId=%s

	@Resource
	private JdbcTemplate mainJdbc;
	
	public boolean isTestChannel(Integer channelId, Integer subChannelId) {
		StringBuilder url = new StringBuilder(WAP_URL);
		if (channelId != null) {
			url.append("?channelId=").append(channelId);
		}
		if (subChannelId != null) {
			url.append("&subChannelId=").append(subChannelId);
		}
		String sql = new StringBuilder()
				.append("select count(1) from abt_zhenai.user_project_case")
				.append(" where caseStatus=2 and url = ?")
				.toString();
		Integer result = mainJdbc.queryForObject(sql, Integer.class, url.toString());
		if (result != null && result > 0) {
			return true;
		} else {
			return false;
		}
	}
}
