package com.web.abt.m.service;

import com.web.abt.m.common.ResultBean;



/**
 */
public interface UserService {

	public ResultBean doLoginUser(String account, String password);
	
}