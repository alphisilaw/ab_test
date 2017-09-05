package com.zhenai.channel_manager.context;

import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

public class Config {

	public static ResourceBundle prop = ResourceBundle.getBundle("config");
	
	public static String getDisparchHost(){
		return StringUtils.trim(prop.getString("dispatch_host"));
	}
	
	public static String getClearTestChannelCacheUrl(){
		return StringUtils.trim(prop.getString("clear_test_channel_cache"));
	}

	public static String getClearSpecialListCacheUrl(){
		return StringUtils.trim(prop.getString("clear_special_list_cache"));
	}

	public static String getClearStrategyCacheUrl(){
		return StringUtils.trim(prop.getString("clear_strategy_cache"));
	}

	public static String getWapUrl(){
		return StringUtils.trim(prop.getString("wap_url"));
	}

	public static String getWapPreview(){
		return StringUtils.trim(prop.getString("wap_preview"));
	}
}
