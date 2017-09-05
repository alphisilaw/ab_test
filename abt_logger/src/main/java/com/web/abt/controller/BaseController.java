package com.web.abt.controller;

import com.alibaba.fastjson.JSONObject;


public class BaseController {

	protected static final String RESULT = "result";
	protected static final int RESULT_SUCCESS = 1;
	protected static final int RESULT_FAIL = 0;
	protected static final String ERRMSG = "errMsg";
	protected static final String DATA = "data";
	
	protected String SUCCESS(Object data){
		JSONObject json = new JSONObject();
		json.put(RESULT, RESULT_SUCCESS);
		if (data != null) {
			json.put(DATA, data);
		} else {
			json.put(DATA, "");
		}
		json.put(ERRMSG, "");
		return json.toString();
	}
	
	protected String FAIL(String errCode){
		JSONObject json = new JSONObject();
		json.put(RESULT, RESULT_FAIL);
		json.put(DATA, "");
		json.put(ERRMSG, errCode);
		return json.toString();
	}
}
