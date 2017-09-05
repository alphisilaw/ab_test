package com.web.abt.m.service.impl;

import com.coola.jutil.encrypt.BASE64Coding;
import com.web.abt.m.common.ResultBean;
import com.web.abt.m.common.ResultConstant;
import com.web.abt.m.dao.UserInfoDao;
import com.web.abt.m.model.UserInfoModel;
import com.web.abt.m.service.UserService;

/**
 */
public class UserServiceImpl extends BaseServiceImpl implements UserService {

	@Override
	public ResultBean doLoginUser(String account, String password) {
		
		UserInfoModel userInfo = UserInfoDao.getInstance().findByEmailByBackRead(account);
		if(userInfo != null){
			String pwdStr = new String(BASE64Coding.decode(userInfo.getPassword()));
			if(password.equals(pwdStr)){
				return Result(userInfo);
			}else{
				return Result(ResultConstant.PASSWORD_INCORRECT);
			}
		}else{
			return Result(ResultConstant.USER_NOT_EXIST);
		}
	}
	
}
