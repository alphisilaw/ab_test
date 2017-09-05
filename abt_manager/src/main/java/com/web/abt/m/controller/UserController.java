package com.web.abt.m.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.abt.m.common.ResultBean;
import com.web.abt.m.common.ResultConstant;
import com.web.abt.m.model.UserInfoModel;
import com.web.abt.m.model.UserProjectCaseGoalTypeModel;
import com.web.abt.m.service.ServiceFactory;
import com.web.abt.m.util.CookieUtil;
import com.web.abt.m.util.Encryptstr;

@Controller
public class UserController extends BaseController {
	
	@RequestMapping(method = RequestMethod.POST, value = "doLogin")
	@ResponseBody
	public String doLogin(String email, String password, HttpServletResponse response){
		if(StringUtils.isBlank(email)){
			return FAIL(ResultConstant.ACCOUNT_EMPTY);
		}
		if(StringUtils.isBlank(password)){
			return FAIL(ResultConstant.PASSWORD_EMPTY);
		}
		
		ResultBean bean = ServiceFactory.getUserService().doLoginUser(email, password);
		if(bean.isSuccess()){
			UserInfoModel userInfo = (UserInfoModel) bean.getBean();
			CookieUtil.addCookie(response, this.LOGIN_UID, Encryptstr.encrypt(String.valueOf(userInfo.getUid())));
			if( userInfo.getStatus()==-1 ){
				CookieUtil.addCookie(response, "UserStatus", "-1");
			}
			CookieUtil.addCookie(response, this.LOGIN_NAME, String.valueOf(userInfo.getNickName()));
//			session.setAttribute(this.LOGIN_UID, userInfo.getUid());
//			session.setAttribute(this.LOGIN_NAME, userInfo.getNickName());
			return SUCCESS();
		}else{
			return FAIL(bean.getErrCode());
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "doLogout")
	@ResponseBody
	public String doLogout(HttpServletRequest request, HttpServletResponse response){
		CookieUtil.removeCookie(request, response, this.LOGIN_UID);
		CookieUtil.removeCookie(request, response, this.LOGIN_NAME);
//		session.removeAttribute("loginUid");
//		session.removeAttribute("loginName");
		return SUCCESS();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getEditPageInitAttrs")
	@ResponseBody
	public String getEditPageInitAttrs(HttpServletRequest request){
		JSONObject json = new JSONObject();
		
		// 普通目标 - 类型
		ResultBean goalTypeBean = ServiceFactory.getGoalTypeService().doSearchGoalTypesByBuizType(1);
		if(goalTypeBean.isSuccess()){
			List<UserProjectCaseGoalTypeModel> goalTypeList = (List<UserProjectCaseGoalTypeModel>) goalTypeBean.getBean();
			
			List<Map<String, Object>> options = new ArrayList<Map<String, Object>>();
			for(UserProjectCaseGoalTypeModel goalType : goalTypeList){
				Map<String, Object> option = new HashMap<String, Object>();
				option.put("key", goalType.getId());
				option.put("value", goalType.getGoalTypeName());
				options.add(option);
			}
			json.put("normalGoalTypes", options);
		}
		
		// KPI目标 - 类型
		ResultBean kpiTypeBean = ServiceFactory.getGoalTypeService().doSearchGoalTypesByBuizType(2, 3);
		if(kpiTypeBean.isSuccess()){
			List<UserProjectCaseGoalTypeModel> goalTypeList = (List<UserProjectCaseGoalTypeModel>) kpiTypeBean.getBean();
			
			List<Map<String, Object>> options = new ArrayList<Map<String, Object>>();
			for(UserProjectCaseGoalTypeModel goalType : goalTypeList){
				Map<String, Object> option = new HashMap<String, Object>();
				option.put("key", goalType.getId());
				option.put("value", goalType.getGoalTypeName());
				options.add(option);
			}
			json.put("kpiGoalTypes", options);
		}
				
		json.put(this.RESULT, this.RESULT_SUCCESS);
		return json.toString();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "resetCookie")
	public void resetCookie(String cookieStr, HttpServletResponse response){
		if(StringUtils.isNotBlank(cookieStr)){
			String[] cookieList = StringUtils.split(cookieStr, ";");
			for(String ckStr : cookieList){
				String[] s = StringUtils.split(ckStr.trim(), "=");
				if(s.length < 2){
					continue;
				}
				Cookie c = new Cookie(s[0], s[1]);
				c.setPath("/");
				response.addCookie(c);
			}
		} 
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value="demoLogin")
	public void demoLogin(HttpServletResponse response){
		CookieUtil.addCookie(response, this.LOGIN_UID, Encryptstr.encrypt(String.valueOf(25)));
		CookieUtil.addCookie(response, this.LOGIN_NAME, "Demo");
		response.setHeader("Location", "/channel_manager");
		response.setStatus(302);
	}
}
