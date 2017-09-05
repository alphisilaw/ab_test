package com.zhenai.channel_dispatcher.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zhenai.channel_dispatcher.service.ChannelVersionService;

@Controller
public class RoutedController extends BaseController {

	private static final Logger logger = Logger.getLogger(RoutedController.class);

	@Resource
	private ChannelVersionService versionService;
	

	@RequestMapping("getTemplate")
	@ResponseBody
	@Deprecated
	public Integer getTemplate(Integer channelId, 
			Integer subid, 
			String sessionId,
			Integer tempId,
			HttpServletRequest request) {
		String info = new StringBuilder("method:getTemplate,")
		.append("param:channelId=").append(channelId)
		.append("&subid=").append(subid)
		.append("&sessionId=").append(sessionId)
		.append("&tempId=").append(tempId)
		.toString();
		logger.info(info);

		return versionService.getTemplate(channelId, subid, sessionId, tempId, request.getRemoteAddr());
	}

	@RequestMapping("getChannelStatus")
	@ResponseBody
	public String getChannelStatus(Integer channelId, 
			Integer subid, 
			String sessionId,
			Integer tempId,
			HttpServletRequest request) {

		String info = new StringBuilder("method:getChannelStatus,")
		.append("param:channelId=").append(channelId)
		.append("&subid=").append(subid)
		.append("&sessionId=").append(sessionId)
		.append("&tempId=").append(tempId)
		.toString();
		logger.info(info);

		
		int rtempId = versionService.getTemplate(channelId, subid, sessionId, tempId, request.getRemoteAddr());
		boolean isTestChannel = false;
		int status = 1;
		if (rtempId > 0) {
			try {
				isTestChannel = versionService.isTestChannel(channelId, subid);
			} catch (Exception e) {
				logger.error("isTestChannel error", e);
				status = 0;
			}
		}
		JSONObject json = new JSONObject();
		json.put("status", status);
		json.put("tempId", rtempId);
		json.put("isTestChannel", isTestChannel);
		return json.toJSONString();
	}

}
