package com.zhenai.channel_manager.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhenai.channel_manager.util.HttpsclientUtil;

@Service("templateService")
public class TemplateServiceImpl implements TemplateService {
	
	private static final Logger logger = Logger.getLogger(TemplateServiceImpl.class);
	
	private static final String url = "http://m.zhenai.com/templateList.do";
	
	@Resource
	private JdbcTemplate mainJdbc;

	@Override
	public Map<Integer, String> getTemplates() {
		Map<Integer, String> templates = new HashMap<Integer, String>();
		try {
			String result = HttpsclientUtil.get(url);
			JSONObject json = JSONObject.parseObject(result);
			int status = json.getIntValue("status");
			if (status == 1) {
				JSONArray array = json.getJSONArray("data");
				for (Object object : array) {
					JSONObject temp = (JSONObject) object;
					templates.put(temp.getInteger("id"), temp.getString("name"));
				}
			}
		} catch (Exception e) {
			logger.error("getTemplates error", e);
		}
		return templates;
	}
	
	@Override
	public List<String> getStaples() {
		return mainJdbc.queryForList("select name from Staple1", String.class);
	}

}
