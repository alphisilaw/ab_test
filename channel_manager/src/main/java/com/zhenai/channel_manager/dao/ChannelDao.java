package com.zhenai.channel_manager.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.zhenai.channel_manager.controller.moudel.ChannelParam;
import com.zhenai.channel_manager.util.CommonUtil;

@Repository
public class ChannelDao {

	@Resource
	private JdbcTemplate mainJdbc;

	public List<Map<String, Object>> getWapChannelList(ChannelParam param) {
		
		int channelId = CommonUtil.evalInt(param.getChannelId());
		
		List<Object> pobj = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder()
		.append("select sc.Staple1,sc.ChannelName,sc.channelId,cs.subid,sc.platform,ct.testId ")
		.append("from SpreadChannel sc ")
		.append("left join ChannelSubid cs on sc.channelId = cs.channelId ")
		.append("left join ChannelTest ct on ct.channelId=sc.channelId ")
		.append("and (ct.subChannelId=cs.subid or (ct.subChannelId is null and cs.subid is null) ")
		.append(") where sc.platform = 1 ");
		if (channelId > 0) {
			sql.append("and sc.channelId = ? ");
			pobj.add(param.getChannelId());
		}
		if (CommonUtil.evalInt(param.getSubChannelId()) > 0) {
			sql.append("and cs.subid = ? ");
			pobj.add(param.getSubChannelId());
		}
		if (StringUtils.isNotEmpty(param.getChannelName())) {
			sql.append("and sc.ChannelName like '%").append(StringUtils.trim(param.getChannelName())).append("%' ");
		}
		if (StringUtils.isNotEmpty(param.getStaple1())) {
			sql.append("and sc.staple1 like '%").append(StringUtils.trim(param.getStaple1())).append("%' ");
		}
		if (CommonUtil.evalInt(param.getTempId()) > -1) {
			sql.append("and exists(select 1 from ChannelVersion cv where cv.testId=ct.testId and cv.tempId = ? and cv.status = 1) ");
			pobj.add(param.getTempId());
		}
		sql.append(" order by ct.testId desc limit ?,? ");// null值太多好慢
		pobj.add(param.getPageNum()*10);
		pobj.add(param.getPageSize());
		
		List<Map<String, Object>> resultList = mainJdbc.queryForList(sql.toString(), 
				pobj.toArray(new Object[pobj.size()]));
		return resultList;
	}

	public Integer getWapChannelCount(ChannelParam param) {
		
		int channelId = CommonUtil.evalInt(param.getChannelId());
		
		List<Object> pobj = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder()
		.append("select count(1) ")
		.append("from SpreadChannel sc ")
		.append("left join ChannelSubid cs on sc.channelId = cs.channelId ")
		.append("left join ChannelTest ct on ct.channelId=sc.channelId ")
		.append("and (ct.subChannelId=cs.subid or (ct.subChannelId is null and cs.subid is null) ")
		.append(") where sc.platform = 1 ");
		
		if (channelId > 0) {
			sql.append("and sc.channelId = ? ");
			pobj.add(param.getChannelId());
		}
		if (CommonUtil.evalInt(param.getSubChannelId()) > 0) {
			sql.append("and cs.subid = ? ");
			pobj.add(param.getSubChannelId());
		}
		if (StringUtils.isNotEmpty(param.getChannelName())) {
			sql.append("and sc.ChannelName = ? ");
			pobj.add(param.getChannelName());
		}
		if (StringUtils.isNotEmpty(param.getStaple1())) {
			sql.append("and sc.staple1 = ? ");
			pobj.add(param.getStaple1());
		}
		if (CommonUtil.evalInt(param.getTempId()) > -1) {
			sql.append("and exists(select 1 from ChannelVersion cv where cv.testId=ct.testId and cv.tempId = ? and cv.status = 1) ");
			pobj.add(param.getTempId());
		}
		
		int count = 0;
		try {
			count = mainJdbc.queryForObject(sql.toString(), 
											Integer.class,
											pobj.toArray(new Object[pobj.size()]));
		} catch (Exception e) {
			
		}
		return count;
	}

	public Map<String, Object> getWapChannel(Integer channelId, Integer subChannelId) {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder()
		.append("select sc.Staple1,sc.ChannelName,sc.channelId,cs.subid,sc.platform ")
		.append("from SpreadChannel sc ")
		.append("left join ChannelSubid cs on sc.channelId = cs.channelId ")
		.append("where sc.platform = 1 and sc.channelId = ? ");
		param.add(channelId);
		if (subChannelId != null) {
			sql.append("and cs.subid = ? ");
			param.add(subChannelId);
		}
		Map<String, Object> resultMap = null;
		try {
			resultMap = mainJdbc.queryForMap(sql.toString(), param.toArray(new Object[param.size()]));
		} catch (Exception e) {
			
		}
		return resultMap;
	}
	
	
	public List<Integer> getSubChannelIdsByChannelId(Integer channelId) {
		String sql = "select subid from ChannelSubid where channelId=?";
		try {
			return mainJdbc.queryForList(sql, Integer.class, channelId);
		} catch (Exception e) {
			return null;
		}
	}
	
}
