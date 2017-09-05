package com.zhenai.channel_manager.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import com.zhenai.channel_manager.context.Config;
import com.zhenai.channel_manager.controller.moudel.ChannelParam;
import com.zhenai.channel_manager.dao.ChannelDao;
import com.zhenai.channel_manager.dao.ChannelVersionDao;
import com.zhenai.channel_manager.moudel.ChannelTest;
import com.zhenai.channel_manager.moudel.ChannelVersion;
import com.zhenai.channel_manager.util.CommonUtil;
import com.zhenai.channel_manager.util.DeepCopy;

@Service("channelService")
public class ChannelServiceImpl implements ChannelService {
	
	@Resource
	private ChannelVersionDao channelVersionDao;

	@Resource
	private ChannelTestService testService;
	
	@Resource
	private ChannelDao channelDao;
	
	@Resource
	private DataViewService dataViewService;
	

	@Override
	public List<Map<String, Object>> getWapChannelList(ChannelParam param) {
		
		List<Map<String, Object>> resultList = channelDao.getWapChannelList(param);
		
		List<Integer> testIds = new ArrayList<Integer>();
		
		for (Map<String, Object> map : resultList) {
			
			Integer testId = (Integer) map.get("testId");
			if (testId != null) {
				testIds.add(testId);
			}
		}
		
		List<Map<String, Object>> versionList = channelVersionDao.findLstByTestId(testIds);
		
		for (Map<String, Object> map : resultList) {
			
			StringBuilder real = getReal(map);
			map.put("url", real);
			
			StringBuilder preview = getPreview(map);
			map.put("preview", preview);
			
			setVersions(versionList, map);
		}
		
		return resultList;
	}

	private void setVersions(List<Map<String, Object>> versionList,
			Map<String, Object> map) {
		
		List<Map<String, Object>> versions = null;
		if (!map.containsKey("versions")) {
			versions = new ArrayList<Map<String, Object>>();
			map.put("versions", versions);
		}
		
		int testId = CommonUtil.evalInt(map.get("testId"));
		map.remove("testId");
		Iterator<Map<String, Object>> iterator = versionList.iterator();
		while (iterator.hasNext()) {
			Map<String, Object> version = iterator.next();
			if (testId == CommonUtil.evalInt(version.get("testId"))) {
				versions.add(version);
				iterator.remove();
			}
		}
	}

	private StringBuilder getReal(Map<String, Object> map) {
		StringBuilder url = new StringBuilder(Config.getWapUrl());
		if (map.get("channelId") != null) {
			url.append("?channelId=").append(map.get("channelId"));
		}
		if (map.get("subid") != null) {
			url.append("&subChannelId=").append(map.get("subid"));
		}
		return url;
	}

	private StringBuilder getPreview(Map<String, Object> map) {
		StringBuilder url = new StringBuilder(Config.getWapPreview());
		if (map.get("channelId") != null) {
			url.append("?channelId=").append(map.get("channelId"));
		}
		if (map.get("subid") != null) {
			url.append("&subChannelId=").append(map.get("subid"));
		}
		return url;
	}
	
	@Override
	public Integer getWapChannelCount(ChannelParam param) {
		return channelDao.getWapChannelCount(param);
	}
	
	@Override
	public List<Map<String, Object>> getWapChannelData(Integer channelId, 
												Integer subChannelId,
												Date begin, 
												Date end) {
		List<Map<String, Object>> resultLst = new ArrayList<Map<String,Object>>();
		if (end != null || begin.before(end)) {

			Map<String, Object> channels = channelDao.getWapChannel(channelId, subChannelId);
			
			if (channels != null) {
				
				List<ChannelVersion> versions = channelVersionDao.findLstForData(channelId, subChannelId);
				
				Map<Date, List<ChannelVersion>> versionMap = new TreeMap<Date, List<ChannelVersion>>();
				for (ChannelVersion version : versions) {
					Date create = version.getCreatetime();
					List<ChannelVersion> groupLst = null;
					if (!versionMap.containsKey(create)) {
						groupLst = new ArrayList<ChannelVersion>(6);
						versionMap.put(create, groupLst);
					}
					groupLst = versionMap.get(create);
					groupLst.add(version);
				}
				Date rbegin = begin;
				Date rend = null;
				if (versionMap.size() > 0) {
					boolean endLoop = false;
					List<ChannelVersion> groupLst = null;
					for (Entry<Date, List<ChannelVersion>> channelVersion : versionMap.entrySet()) {
						Date versionDate = channelVersion.getKey();
						if (end.after(versionDate) || DateUtils.isSameDay(end, versionDate)) {
							rend = versionDate;
						} else {
							rend = end;
							endLoop = true;
						}
						if (rbegin.after(rend) || groupLst == null) {
							groupLst = channelVersion.getValue();
							continue;
						}
						Map<String, Object> tchannels = DeepCopy.copyImplSerializable(channels);
						ChannelTest channelTest = testService.findOrSetByChannelId(channelId, subChannelId);
						getVersionData(rbegin, rend, tchannels, groupLst, channelTest);
						resultLst.add(tchannels);
						if (endLoop) {
							break;
						}
						rbegin = rend;
						groupLst = channelVersion.getValue();
					}
					if (!endLoop) {
						rend = end;
						Map<String, Object> tchannels = DeepCopy.copyImplSerializable(channels);
						ChannelTest channelTest = testService.findOrSetByChannelId(channelId, subChannelId);
						getVersionData(rbegin, rend, tchannels, groupLst, channelTest);
						resultLst.add(tchannels);
					}
				} 
			}
		}
		
		return resultLst;
	}

	private void getVersionData(Date rbegin, Date rend,
			Map<String, Object> tchannels, List<ChannelVersion> versions, ChannelTest channelTest) {
		String minDate = DateFormatUtils.format(rbegin, "yyyy.MM.dd");
		String maxDate = DateFormatUtils.format(rend, "yyyy.MM.dd");
		String date = minDate + "~" + maxDate;
		tchannels.put("date", date);
		
		List<Map<String, Object>> kpidatas = getKpiDatas(rbegin, rend, versions, channelTest);
		
		tchannels.put("kpiDatas", kpidatas);
	}

	private List<Map<String, Object>> getKpiDatas(Date begin, 
												 Date end,
												 List<ChannelVersion> versions, 
											 	 ChannelTest channelTest) {
		List<Map<String, Object>> kpidatas = new ArrayList<Map<String,Object>>();
		if (channelTest != null) {
			int testId = channelTest.getTestId();
			Map<Integer, Map<String, Object>> kpidataMap = dataViewService.getKPIData(testId, begin, end);
			for (ChannelVersion version : versions) {
				Map<String, Object> kpidata = kpidataMap.get(version.getVersionId());
				if (kpidata == null) {
					kpidata = new HashMap<String, Object>();
				}
				kpidata.put("tempName", version.getTempName());
				kpidatas.add(kpidata);
			}
		}
		return kpidatas;
	}

	@Override
	public Workbook transformListToWorkbook(List<Map<String, Object>> resultList) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("日期");
		row.createCell(1).setCellValue("渠道名称");
		row.createCell(2).setCellValue("渠道号");
		row.createCell(3).setCellValue("子渠道号");
		row.createCell(4).setCellValue("模板名称");
		row.createCell(5).setCellValue("pv");
		row.createCell(6).setCellValue("uv");
		row.createCell(7).setCellValue("注册人数");
		row.createCell(8).setCellValue("注册/uv");
		row.createCell(9).setCellValue("号码验证数");
		row.createCell(10).setCellValue("号码验证率");
		row.createCell(11).setCellValue("自助收入");
		row.createCell(12).setCellValue("线下收入");
		row.createCell(13).setCellValue("电销收入");
		row.createCell(14).setCellValue("总收入");
		row.createCell(15).setCellValue("Arpu");
		
		for (Map<String, Object> result : resultList) {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> kpidatas = (List<Map<String, Object>>) result.get("kpiDatas");
			int size = kpidatas.size();
			for (int i = 1; i < size+1; i++) {
				if (size == 0) {
					row = sheet.createRow(1);
					row.createCell(0).setCellValue(CommonUtil.evalString(result.get("date")));
					row.createCell(1).setCellValue(CommonUtil.evalString(result.get("ChannelName")));
					row.createCell(2).setCellValue(CommonUtil.evalString(result.get("channelId")));
					row.createCell(3).setCellValue(CommonUtil.evalString(result.get("subid")));
				} else if (size > 0) {
						row = sheet.createRow(i);
						Map<String, Object> kpiData = kpidatas.get(size-1);
						row.createCell(0).setCellValue(CommonUtil.evalString(result.get("date")));
						row.createCell(1).setCellValue(CommonUtil.evalString(result.get("ChannelName")));
						row.createCell(2).setCellValue(CommonUtil.evalString(result.get("channelId")));
						row.createCell(3).setCellValue(CommonUtil.evalString(result.get("subid")));
						row.createCell(4).setCellValue(CommonUtil.evalString(kpiData.get("tempName")));
						row.createCell(5).setCellValue(CommonUtil.evalString(kpiData.get("pv")));
						row.createCell(6).setCellValue(CommonUtil.evalString(kpiData.get("uv")));
						row.createCell(7).setCellValue(CommonUtil.evalString(kpiData.get("regitCount")));
						row.createCell(8).setCellValue(CommonUtil.evalString(kpiData.get("regitPercent")));
						row.createCell(9).setCellValue(CommonUtil.evalString(kpiData.get("verpassCount")));
						row.createCell(10).setCellValue(CommonUtil.evalString(kpiData.get("verpassPercent")));
						row.createCell(11).setCellValue(CommonUtil.evalString(kpiData.get("selfSevcIncome")));
						row.createCell(12).setCellValue(CommonUtil.evalString(kpiData.get("offlineIncome")));
						row.createCell(13).setCellValue(CommonUtil.evalString(kpiData.get("telsellIncome")));
						row.createCell(14).setCellValue(CommonUtil.evalString(kpiData.get("totalIncome")));
						row.createCell(15).setCellValue(CommonUtil.evalString(kpiData.get("totalPercent")));
				}
			}
		}
		return wb;
	}
}
