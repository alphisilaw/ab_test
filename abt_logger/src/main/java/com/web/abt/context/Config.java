package com.web.abt.context;

import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

public class Config {

	public static ResourceBundle prop = ResourceBundle.getBundle("config");
	
	public static String getUploadChannel(){
		return StringUtils.trim(prop.getString("uploadChannel"));
	}
}
