package com.zhenai.channel_dispatcher.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @version V1.0
 */
public class CommonUtil {
    
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
	 * 去掉url中的路径，留下请求参数部分
	 * @param strURL url地址
	 * @return url请求参数部分
	 */
	private static String TruncateUrlPage(String strURL) {
		String strAllParam=null;
		if (strURL != null) {
			String[] arrSplit=null;
			strURL=strURL.trim();

			arrSplit=strURL.split("[?]");
			if(strURL.length()>1) {
				if(arrSplit.length>1) {
					if(arrSplit[1]!=null) {
						strAllParam=arrSplit[1];
					}
				}
			}
		}

		return strAllParam;   
	}
	/**
	 * 解析出url参数中的键值对
	 * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
	 * @param URL  url地址
	 * @return  url请求参数部分
	 */
	public static Map<String, String> URLRequest(String URL) {
		Map<String, String> mapRequest = new HashMap<String, String>();

		String[] arrSplit=null;

		String strUrlParam=TruncateUrlPage(URL);
		if(strUrlParam==null) {
			return mapRequest;
		}
		arrSplit=strUrlParam.split("[&]");
		for(String strSplit:arrSplit) {
			String[] arrSplitEqual=null;         
			arrSplitEqual= strSplit.split("[=]");

			//解析出键值
			if(arrSplitEqual.length>1) {
				//正确解析
				mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

			} else {
				if(arrSplitEqual[0]!="") {
					//只有参数没有值，不加入
					mapRequest.put(arrSplitEqual[0], "");       
				}
			}
		}   
		return mapRequest;   
	}
}
