package com.web.abt.m.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.web.abt.m.model.UserProjectCaseGoalModel;
import com.web.abt.m.model.UserProjectCaseModel;
import com.web.abt.m.model.UserProjectCaseVersionModel;
import com.web.abt.m.model.UserProjectModel;

/**
 * @version V1.0
 */
public class CommonUtil {
    
    public static Map<String, Object> getUserProjectMap(UserProjectModel model) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("projectId", model.getProjectId());
    	map.put("projectName", model.getProjectName());
    	map.put("description", model.getDescription());
    	map.put("platform", model.getPlatform());
    	map.put("ownerId", model.getOwnerId());
    	map.put("createTime", model.getCreateTime()==null?null:model.getCreateTime().getTime());
    	map.put("updateTime", model.getUpdateTime()==null?null:model.getUpdateTime().getTime());
    	return map;
    }
    
    public static Map<String, Object> getUserProjectCaseMap(UserProjectCaseModel model) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("caseId", model.getCaseId());
    	map.put("caseName", model.getCaseName());
    	map.put("projectId", model.getProjectId());
    	map.put("caseStatus", model.getCaseStatus());
    	map.put("dayCount", model.getDayCount());
    	map.put("buizType", model.getBuizType());
    	map.put("verCount", model.getVerCount());
    	map.put("curGoalCount", model.getCurGoalCount());
    	map.put("maxGoalCount", model.getMaxGoalCount());
    	map.put("url", model.getUrl());
    	map.put("createTime", model.getCreateTime()==null?null:model.getCreateTime().getTime());
    	map.put("updateTime", model.getUpdateTime()==null?null:model.getUpdateTime().getTime());
    	map.put("isMobile", model.getIsMobile());
    	return map;
    }
    
    public static Map<String, Object> getUserProjectCaseGoalMap(UserProjectCaseGoalModel model) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("goalId", model.getGoalId());
    	map.put("goalName", model.getGoalName());
    	map.put("goalTypeId", model.getGoalTypeId());
    	map.put("goalSelector", model.getGoalSelector());
    	map.put("seq", model.getSeq());
    	map.put("caseId", model.getCaseId());
    	map.put("projectId", model.getProjectId());
    	map.put("createTime", model.getCreateTime()==null?null:model.getCreateTime().getTime());
    	map.put("updateTime", model.getUpdateTime()==null?null:model.getUpdateTime().getTime());
    	map.put("isMaster", model.getIsMaster());
    	return map;
    }
    
    public static Map<String, Object> getUserProjectCaseGoalMapForDataView(UserProjectCaseGoalModel model) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("goalId", model.getGoalId());
    	String goalName = model.getGoalName();
    	if(model.getIsMaster() > 0){
    		goalName += "(主追踪目标)";
    	}
    	map.put("goalName", goalName);
    	map.put("goalTypeId", model.getGoalTypeId());
    	map.put("goalSelector", model.getGoalSelector());
    	map.put("seq", model.getSeq());
    	map.put("caseId", model.getCaseId());
    	map.put("projectId", model.getProjectId());
    	map.put("createTime", model.getCreateTime()==null?null:model.getCreateTime().getTime());
    	map.put("updateTime", model.getUpdateTime()==null?null:model.getUpdateTime().getTime());
    	map.put("isMaster", model.getIsMaster());
    	return map;
    }
    
    public static Map<String, Object> getUserProjectCaseVersionMap(UserProjectCaseVersionModel model) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("versionId", model.getVersionId());
    	map.put("versionName", model.getVersionName());
    	map.put("versionType", model.getVersionType());
    	map.put("versionStatus", model.getVersionStatus());
    	map.put("caseId", model.getCaseId());
    	map.put("projectId", model.getProjectId());
    	map.put("jsCode", model.getJsCode());
    	map.put("createTime", model.getCreateTime()==null?null:model.getCreateTime().getTime());
    	map.put("updateTime", model.getUpdateTime()==null?null:model.getUpdateTime().getTime());
    	map.put("percent", model.getPercent());
    	map.put("forwardUrl", model.getForwardUrl());
    	return map;
    }
    
    @SuppressWarnings("serial")
	final static private ArrayList<Integer> percent_arr = new ArrayList<Integer>() {{
	    for(int i=0;i<100;i++){
	    	add(i);
	    }
	}};
	private static List<Integer> RandomNum(int m, List<Integer> arr) {
		List<Integer> v = new ArrayList<Integer>();
		Random r = new Random();
		do {
			if(arr.size() <= 0)
				break;
			int x = r.nextInt(arr.size());
			--m;
			v.add(arr.get(x));
			arr.remove(x);
		} while (m > 0);
		return v;
	}
    public static Map<Integer, Integer> getDistriArrangement(List<UserProjectCaseVersionModel> list){
    	Map<Integer, Integer> verArr = new HashMap<Integer, Integer>();
		List<Integer> perc_arr = new ArrayList<Integer>();
		perc_arr.addAll(percent_arr);
		for(UserProjectCaseVersionModel ver : list){
			//分配 流量 计划
			List<Integer> tmpArr = RandomNum(ver.getPercentInt(),perc_arr);
			for(int arr : tmpArr){
				verArr.put(arr, ver.getVersionId());
			}
		}
		return verArr;
    }
    
	public static double evalDouble(Object obj) {
		if (obj == null)
			return 0d;
		else if (StringUtils.isNotBlank(obj.toString()))
			return Double.parseDouble(obj.toString().trim());
		return 0d;
	}
	
	public static double evalDouble(Object obj, double defaultValue) {
		if (obj == null)
			return defaultValue;
		else if (StringUtils.isNotBlank(obj.toString()))
			return Double.parseDouble(obj.toString().trim());
		return 0d;
	}
	
	public static double evalFloat(Object obj) {
		if (obj == null)
			return 0f;
		else if (StringUtils.isNotBlank(obj.toString()))
			return Float.parseFloat(obj.toString().trim());
		return 0f;
	}
	
	public static float evalFloat(Object obj, float defaultValue) {
		if (obj == null)
			return defaultValue;
		else if (StringUtils.isNotBlank(obj.toString()))
			return Float.parseFloat(obj.toString().trim());
		return 0f;
	}

	/**
	 * 方法说明：方便处理字符串 创建日期：2009-3-16,下午02:54:24,hyc
	 * 
	 * @param obj
	 * @return string
	 */
	public static String evalString(Object obj) {
		if (obj == null)
			return "";
		return obj.toString();
	}
	
	public static String evalToNull(Object obj) {
		if (obj == null) return null;
		return StringUtils.trimToNull(obj.toString());
	}

	/**
	 * 方法说明：方便处理字符串 创建日期：2009-3-16,下午02:54:24,hyc
	 * 
	 * @param obj
	 * @return string
	 */
	public static int evalInt(Object obj) {
		if (obj == null)
			return -1000;
		return NumberUtils.toInt(obj.toString(), -1000);
	}

	public static int evalInt(Object obj, int defautValue) {
		if (obj == null)
			return defautValue;
		return NumberUtils.toInt(obj.toString(), defautValue);
	}

	/**
	 * 方法说明：方便处理字符串 创建日期：2009-4-10,上午11:49:45,hyc
	 * 
	 * @param obj
	 * @return
	 */
	public static long evalLong(Object obj) {
		if (obj == null)
			return -1000;
		return NumberUtils.toLong(obj.toString(), -1000);
	}

	/**
	 * 方法说明：方便处理字符串 创建日期：2009-4-10,上午11:49:45,hyc
	 * 
	 * @param obj
	 * @return
	 */
	public static long evalLong(Object obj, long defaultValue) {
		if (obj == null)
			return defaultValue;
		return NumberUtils.toLong(obj.toString(), defaultValue);
	}

	/**
	 * 方法说明：方便处理boolean 创建日期：2009-5-22,下午03:51:26,hyc
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean evalBoolean(Object obj) {
		if (obj == null)
			return false;
		return (Boolean) obj;
	}

	/**
	 * 方法说明：返回一个Number对象 创建日期：2009-5-25,下午02:30:50,hyc
	 * 
	 * @param obj
	 * @return
	 */
	public static Number evalNumber(Object obj) {
		if (obj == null)
			return null;
		try {
			return (Number) obj;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 方法说明：返回一个Number对象 创建日期：2009-5-25,下午02:30:50,hyc
	 * 
	 * @param obj
	 * @return
	 */
	public static Date evalDate(Object obj) {
		if (obj == null)
			return null;
		try {
			return (Date) obj;
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 方法说明：格式化日期 创建日期：2010-8-1,下午01:25:45,hyc
	 * 
	 * @param source
	 * @param formatStr
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDateFormStr(String source, String formatStr) {
		if (StringUtils.isNotBlank(source)) {
			// 一种格式
			SimpleDateFormat df = new SimpleDateFormat(formatStr);
			try {
				return df.parse(source);
			} catch (ParseException e) {
				return null;
			}			
		} else {
			return null;
		}
	}
	/**
	 * 方法说明：任意格式化 创建日期：2010-8-18,下午04:40:31,hyc
	 * 
	 * @param date
	 * @param formatStr
	 * @return
	 */
	public static String formateDateToStr(Date date, String formatStr) {
		if(date == null) return null;
		// 一种格式
		SimpleDateFormat df = new SimpleDateFormat(formatStr);
		return df.format(date);
	}
}
