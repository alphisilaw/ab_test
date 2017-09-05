package com.zhenai.channel_manager.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zhenai.channel_manager.controller.BaseController;


public class SecurityInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Cookie[] cookies = request.getCookies();
		Map<String, String> cookieMap = new HashMap<String, String>();
		if (cookies == null) {
			response.sendRedirect("/abt_manager/login.jsp");
			return false;
		}
		for (Cookie cookie : cookies) {
			cookieMap.put(cookie.getName(), cookie.getValue());
		}
		if (StringUtils.isNoneBlank(cookieMap.get(BaseController.LOGIN_NAME))
				&& StringUtils.isNoneBlank(cookieMap.get(BaseController.LOGIN_UID))) {
			return true;
		} else {
			response.sendRedirect("/abt_manager/login.jsp");
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
