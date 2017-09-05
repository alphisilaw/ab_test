package com.zhenai.channel_manager.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.zhenai.channel_manager.controller.moudel.ChannelParam;

public interface ChannelService {

	List<Map<String, Object>> getWapChannelList(ChannelParam param);

	List<Map<String, Object>> getWapChannelData(Integer channelId, 
			Integer subChannelId,
			Date begin, 
			Date end);

	Integer getWapChannelCount(ChannelParam param);

	Workbook transformListToWorkbook(List<Map<String, Object>> kpidatas);
}
