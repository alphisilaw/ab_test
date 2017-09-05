package com.zhenai.channel_manager.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zhenai.channel_manager.controller.moudel.ChannelParam;
import com.zhenai.channel_manager.service.ChannelService;
import com.zhenai.channel_manager.service.TemplateService;

@Controller
public class ChannelController extends BaseController {
	
	private static final Logger LOGGER = Logger.getLogger(ChannelController.class);
	
	@Resource
	private ChannelService channelService; 
	
	@Resource
	private TemplateService templateService;
	
	@RequestMapping(value = "getChannelList")
    @ResponseBody
    public String getChannelList(ChannelParam param) {
		try {
			List<Map<String, Object>> resultList = channelService.getWapChannelList(param);
			int itemNum = channelService.getWapChannelCount(param);
			JSONObject json = new JSONObject();
			json.put(RESULT, RESULT_SUCCESS);
			json.put(ERRMSG, "");
			json.put("itemNum", itemNum);
			json.put(DATA, resultList);
			return json.toString();
		} catch (Exception e) {
			LOGGER.error("获取出错!", e);
			return FAIL("获取出错!");
		}
	}
	
	@RequestMapping("")
	public String main(HttpServletRequest request) {
		return channel(request);
	}
	
	@RequestMapping("channel")
	public String channel(HttpServletRequest request) {
		Map<Integer, String> templates = templateService.getTemplates();
		List<String> staples = templateService.getStaples();
		request.setAttribute("templates", templates);
		request.setAttribute("staples", staples);
		return "forward:/channel.jsp";
	}
	
	@RequestMapping("channel-data")
	public String channelData(String channelId, 
							  String subChannelId,
							  String begin, 
							  String end,
							  HttpServletRequest request) {
		Date[] dates = initDates(begin, end);
		Integer channel = NumberUtils.isNumber(channelId)? NumberUtils.toInt(channelId):null;
		Integer subChannel =  NumberUtils.isNumber(subChannelId)? NumberUtils.toInt(subChannelId):null;
		List<Map<String, Object>> resultMap = channelService.getWapChannelData(channel, subChannel, dates[0], dates[1]);
		request.setAttribute("resultLst", resultMap);
		request.setAttribute("channelId", channelId);
		request.setAttribute("subChannelId", subChannelId);
		request.setAttribute("begin", DateFormatUtils.format(dates[0], "yyyy-MM-dd"));
		request.setAttribute("end", DateFormatUtils.format(dates[1], "yyyy-MM-dd"));
		return "forward:/channel-data.jsp";
	}

	@RequestMapping("channel-data-excel")
	public void channelDataExcel(String channelId, 
							  String subChannelId,
							  String begin, 
							  String end,
							  HttpServletResponse response) throws IOException {
		Date[] dates = initDates(begin, end);
		Integer channel = NumberUtils.isNumber(channelId)? NumberUtils.toInt(channelId):null;
		Integer subChannel =  NumberUtils.isNumber(subChannelId)? NumberUtils.toInt(subChannelId):null;
		List<Map<String, Object>> resultMap = channelService.getWapChannelData(channel, subChannel, dates[0], dates[1]);
		Workbook wb = channelService.transformListToWorkbook(resultMap);
		response.setContentType("application/vnd.ms-excel");
		String fileName = "channel-data-" + channel + (subChannel==null?"":("-"+subChannel)) + ".xls";
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		wb.write(response.getOutputStream());
	}
	
	private Date[] initDates(String begin, String end) {
		Date beginDate = null;
		Date endDate = null;
		if (StringUtils.isBlank(end)) {
			endDate = DateUtils.addDays(new Date(), -1);
		} else {
			try {
				endDate = DateUtils.parseDate(end, "yyyy-MM-dd");
			} catch (ParseException e) {
			}
		}
		if (StringUtils.isBlank(begin)) {
			beginDate = endDate;
		} else {
			try {
				beginDate = DateUtils.parseDate(begin, "yyyy-MM-dd");
			} catch (ParseException e) {
			}
		}
		Date[] dates = new Date[2];
		dates[0] = beginDate;
		dates[1] = endDate;
		return dates;
	}
}
