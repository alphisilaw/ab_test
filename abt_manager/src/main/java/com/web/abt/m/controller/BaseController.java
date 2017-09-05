package com.web.abt.m.controller;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import com.web.abt.m.util.CookieUtil;
import com.web.abt.m.util.Encryptstr;

@Controller
public class BaseController {

	protected final String RESULT = "result";
	protected final String RESULT_SUCCESS = "success";
	protected final String RESULT_FAIL = "fail";
	protected final String ERRMSG = "errMsg";
	
	protected final String LOGIN_UID = "loginUid";
	protected final String LOGIN_NAME = "loginName";
	protected final String USER_ACCESS_CASE = "userAccessCase-";
	protected final String SAME_USER_CLICKED = "sameUserClicked";
	
	protected String SUCCESS(){
		JSONObject json = new JSONObject();
		json.put(RESULT, RESULT_SUCCESS);
		return json.toString();
	}
	
	protected String FAIL(String errCode){
		JSONObject json = new JSONObject();
		json.put(RESULT, RESULT_FAIL);
		json.put(ERRMSG, errCode);
		return json.toString();
	}
	
	protected boolean isUserLogin(HttpServletRequest request){
		boolean isLogin = false;
		try {
			String uid = CookieUtil.getCookieValueByName(request, LOGIN_UID);
			uid = Encryptstr.decode(uid);
			if(StringUtils.isNotBlank(uid) && StringUtils.isNumeric(uid) && Integer.valueOf(uid) > 0)
				isLogin = true;
			else
				isLogin = false;
		} catch (Exception e) {
			isLogin = false;
		}
		return isLogin;
	}
	
	protected int getLoginUid(HttpServletRequest request){
		String uid = CookieUtil.getCookieValueByName(request, LOGIN_UID);
		uid = Encryptstr.decode(uid);
		if(StringUtils.isNotBlank(uid) && StringUtils.isNumeric(uid))
			return Integer.valueOf(uid);
		else
			return 0;
	}
	
	protected String getHost(HttpServletRequest request) {
		String host = request.getServerName();
		host = "http://"+host;
		String Port = String.valueOf(request.getServerPort());
		if(!Port.equals("80")){
			host = host+":"+Port;
		}
		return host;
	}
}
