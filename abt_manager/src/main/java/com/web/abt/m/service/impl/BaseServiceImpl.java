package com.web.abt.m.service.impl;

import com.web.abt.m.common.ResultBean;

public class BaseServiceImpl {

	protected ResultBean Result(){
		return new ResultBean();
	}
	
	protected ResultBean Result(String errCode){
		return new ResultBean(errCode);
	}
	
	protected ResultBean Result(Object t){
		return new ResultBean(t);
	}
}
