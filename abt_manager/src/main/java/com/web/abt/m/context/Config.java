package com.web.abt.m.context;

import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

public class Config {


	public static ResourceBundle prop = ResourceBundle.getBundle("config");
	
	public static String getDataHost(){
		return StringUtils.trim(prop.getString("dataHost"));
	}
	
	public static String getLogHost(){
		return StringUtils.trim(prop.getString("logHost"));
	}
	
	public static String getDispatchHost(){
		return StringUtils.trim(prop.getString("dispatchHost"));
	}

	public static String getClearTestCacheUrl() {
		return StringUtils.trim(prop.getString("clearTestCache"));
	}

	public static String getClearChannelCacheUrl() {
		return StringUtils.trim(prop.getString("clearChannelCache"));
	}
}
