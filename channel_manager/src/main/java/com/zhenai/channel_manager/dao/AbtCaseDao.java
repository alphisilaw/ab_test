package com.zhenai.channel_manager.dao;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.zhenai.channel_manager.moudel.ChannelTest;

@Repository
public class AbtCaseDao {
	
	@Resource
	private JdbcTemplate mainJdbc;

	public void saveInAbtCase(ChannelTest channelTest) {
		String sql = new StringBuilder()
				.append("insert into abt_zhenai.user_project_case")
				.append("(caseId,caseName,projectId,caseStatus,isMobile,updateTime,createTime) ")
				.append("values (?,?,?,?,?,?,?)")
				.toString();
		
		Date now = new Date();
		mainJdbc.update(sql, channelTest.getTestId(), channelTest.getTestId(), -1, 2, 1, now, now);
	}
}
