package com.web.abt.m.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class DirectJspFilter extends HandlerInterceptorAdapter {
	protected static final Logger log = Logger.getLogger(DirectJspFilter.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String reqUrl = request.getRequestURI() + ".jsp";
		String cPath = request.getContextPath();
		if(reqUrl.contains("/v/"))
			reqUrl = reqUrl.replace("/v/", "/");
		if(reqUrl.contains(cPath))
			reqUrl = reqUrl.replace(cPath, "");
		request.getRequestDispatcher(reqUrl).forward(request,
				response);
		return false;
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

}